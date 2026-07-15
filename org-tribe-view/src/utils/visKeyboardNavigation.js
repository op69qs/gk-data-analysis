const ESCAPE_CODE = 27
const HORIZONTAL_KEY_CODES = new Set([37, 39])
const COMPATIBILITY_KEY_CODES = new Map([
  [38, 37],
  [40, 39],
])
const ARROW_KEY_CODES = new Set([...HORIZONTAL_KEY_CODES, ...COMPATIBILITY_KEY_CODES.keys()])

function resolveKeyCode(event) {
  return event && (event.keyCode || event.which)
}

export function shouldSuppressVisBrowserShortcut(event) {
  if (!event) return false
  if (event.key === 'Alt' || event.code === 'AltLeft' || event.code === 'AltRight') return true
  return Boolean(event.altKey && HORIZONTAL_KEY_CODES.has(resolveKeyCode(event)))
}

export function createVisKeyboardCoordinator(onKeyCode, onDecision = () => {}) {
  let escapePressed = false
  let arrowGesture = null

  function decide(eventType, code, handled, reason, event) {
    const decision = {
      eventType,
      code: code == null ? null : code,
      handled,
      reason,
      repeat: Boolean(event && event.repeat),
      key: event && event.key,
      which: event && event.which,
      keyCode: event && event.keyCode,
    }
    onDecision(decision)
    return decision
  }

  function handleEscapeKeydown(event, code) {
    if (event.repeat) return decide('keydown', code, false, 'repeat', event)
    if (escapePressed) return decide('keydown', code, false, 'duplicate', event)
    escapePressed = true
    onKeyCode(code)
    return decide('keydown', code, true, 'keydown', event)
  }

  function handleArrowKeydown(event, code) {
    if (event.repeat) return decide('keydown', code, false, 'repeat', event)
    if (arrowGesture && !arrowGesture.resolved && arrowGesture.keydownCode === code) {
      return decide('keydown', code, false, 'duplicate', event)
    }
    if (COMPATIBILITY_KEY_CODES.has(code)) {
      arrowGesture = {
        kind: 'compatibility',
        keydownCode: code,
        fallbackCode: COMPATIBILITY_KEY_CODES.get(code),
        resolved: false,
      }
      return decide('keydown', code, false, 'compat-pending', event)
    }

    arrowGesture = { kind: 'horizontal', keydownCode: code, resolved: true }
    onKeyCode(code)
    return decide('keydown', code, true, 'keydown', event)
  }

  function handleHorizontalKeyup(event, code) {
    if (!arrowGesture) {
      onKeyCode(code)
      return decide('keyup', code, true, 'keyup-fallback', event)
    }

    if (arrowGesture.kind === 'compatibility') {
      if (!arrowGesture.resolved) {
        arrowGesture.resolved = true
        onKeyCode(code)
        return decide('keyup', code, true, 'compat-horizontal-keyup', event)
      }
      return decide('keyup', code, false, 'paired-release', event)
    }

    arrowGesture = null
    return decide('keyup', code, false, 'release', event)
  }

  function handleCompatibilityKeyup(event, code) {
    if (!arrowGesture || arrowGesture.kind !== 'compatibility') {
      return decide('keyup', code, false, 'unsupported', event)
    }

    if (!arrowGesture.resolved) {
      const fallbackCode = arrowGesture.fallbackCode
      arrowGesture = null
      onKeyCode(fallbackCode)
      return decide('keyup', code, true, 'compat-vertical-keyup', event)
    }

    arrowGesture = null
    return decide('keyup', code, false, 'paired-release', event)
  }

  return {
    handleKeydown(event) {
      const code = resolveKeyCode(event)
      if (code === ESCAPE_CODE) return handleEscapeKeydown(event, code)
      if (!ARROW_KEY_CODES.has(code)) return decide('keydown', code, false, 'unsupported', event)
      return handleArrowKeydown(event, code)
    },
    handleKeyup(event) {
      const code = resolveKeyCode(event)
      if (code === ESCAPE_CODE) {
        if (escapePressed) {
          escapePressed = false
          return decide('keyup', code, false, 'release', event)
        }
        onKeyCode(code)
        return decide('keyup', code, true, 'keyup-fallback', event)
      }
      if (HORIZONTAL_KEY_CODES.has(code)) return handleHorizontalKeyup(event, code)
      if (COMPATIBILITY_KEY_CODES.has(code)) return handleCompatibilityKeyup(event, code)
      return decide('keyup', code, false, 'unsupported', event)
    },
    reset() {
      escapePressed = false
      arrowGesture = null
      return decide('reset', null, false, 'reset')
    }
  }
}
