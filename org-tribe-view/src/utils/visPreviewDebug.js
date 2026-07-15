export function isVisPreviewConsoleDebugEnabled(search, storage) {
  try {
    const query = new URLSearchParams(search || '')
    if (query.get('visDebug') === '1') return true
    return Boolean(storage && storage.getItem('VIS_PREVIEW_DEBUG_CONSOLE') === '1')
  } catch (error) {
    return false
  }
}

export function visPreviewDebug(label, payload) {
  if (typeof window === 'undefined') {
    return
  }
  window.__VIS_PREVIEW_DEBUG__ = window.__VIS_PREVIEW_DEBUG__ || []
  window.__VIS_PREVIEW_DEBUG__.push({
    time: new Date().toISOString(),
    label,
    payload
  })
  window.__VIS_PREVIEW_DEBUG_LAST__ = {
    label,
    payload
  }
  if (isVisPreviewConsoleDebugEnabled(window.location && window.location.search, window.localStorage)) {
    console.warn('[VIS_PREVIEW_DEBUG]', label, payload)
  }
}
