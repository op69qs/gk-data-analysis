const PREVIOUS_CODE = 37
const NEXT_CODE = 39

export function createVisWheelCoordinator(onKeyCode, onDecision = () => {}, options = {}) {
  const threshold = options.threshold || 80
  const cooldownMs = options.cooldownMs || 400
  let accumulatedDelta = 0
  let direction = 0
  let cooldownUntil = 0

  function decide(handled, reason, consumed, deltaY = 0) {
    const decision = {
      handled,
      reason,
      consumed,
      deltaY,
      accumulatedDelta,
      direction,
      cooldownUntil,
    }
    onDecision(decision)
    return decision
  }

  return {
    handleWheel(event, now = Date.now()) {
      const deltaY = Number(event && event.deltaY) || 0
      if (deltaY === 0) return decide(false, 'zero-delta', false)
      if (now < cooldownUntil) return decide(false, 'cooldown', true, deltaY)

      const nextDirection = Math.sign(deltaY)
      let reason = 'accumulating'
      if (direction !== 0 && nextDirection !== direction) {
        accumulatedDelta = 0
        reason = 'direction-reset'
      }

      direction = nextDirection
      accumulatedDelta += deltaY
      if (Math.abs(accumulatedDelta) < threshold) {
        return decide(false, reason, true, deltaY)
      }

      onKeyCode(direction > 0 ? NEXT_CODE : PREVIOUS_CODE)
      cooldownUntil = now + cooldownMs
      accumulatedDelta = 0
      direction = 0
      return decide(true, 'threshold', true, deltaY)
    },
    reset() {
      accumulatedDelta = 0
      direction = 0
      cooldownUntil = 0
      return decide(false, 'reset', false)
    }
  }
}
