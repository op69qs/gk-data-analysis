/**
 * Align with electronic-register logout: try to close the current tab after
 * clearing the local session. Browsers only honor window.close() for tabs
 * opened by script (typical portal jump). If the tab stays open, fall back
 * to a hint page so the user can close it manually.
 */
export function closeOrHintWindow(hintPath) {
  window.close()
  setTimeout(function () {
    window.location.replace(hintPath || '/user/closed')
  }, 200)
}
