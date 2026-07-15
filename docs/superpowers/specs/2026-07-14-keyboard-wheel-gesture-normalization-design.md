# Big-screen keyboard and wheel gesture normalization

## Problem

In the affected physical or remote keyboard environment, one horizontal key press does not produce a normal matching event pair. Captured examples include `keydown ArrowDown` followed by `keyup ArrowLeft`, and `keydown ArrowUp` followed by `keyup ArrowRight`. Some presses produce only vertical arrow events. Consequently, repeated left or right presses are mostly ignored, while alternating directions occasionally emits a horizontal keyup and advances an unpredictable number of pages.

The carousel itself is not the failing layer: every captured horizontal event accepted by the coordinator changed `activeIndex` exactly once. Capture and bubble logs also prove that focus and propagation are intact.

## Goals

- Preserve immediate, one-page navigation for normal left and right keydown events.
- Normalize the observed mismatched arrow sequences into one logical gesture.
- Guarantee that one logical keyboard gesture changes at most one page.
- Support mouse-wheel and high-resolution touchpad navigation, one page per deliberate gesture.
- Keep diagnostics opt-in through `visDebug=1` and record normalization decisions.

## Keyboard design

The coordinator owns a small gesture state machine.

1. A normal `ArrowLeft` or `ArrowRight` keydown navigates immediately and opens a resolved gesture. Its matching or mismatched arrow keyups close the gesture without navigating again.
2. An `ArrowDown` or `ArrowUp` keydown opens an unresolved compatibility gesture and does not navigate immediately.
3. If a horizontal keyup follows an unresolved compatibility gesture, its explicit direction wins and navigation occurs once.
4. If only the matching vertical keyup arrives, the captured-device fallback applies: `ArrowDown` means previous page and `ArrowUp` means next page.
5. Additional arrow keyups belonging to the same gesture are ignored. A new non-repeat keydown begins the next gesture.
6. Escape retains its existing behavior and is not combined with arrow gesture state.
7. Blur clears all pending gesture state.

The diagnostic callback reports accepted and ignored events with reasons such as `keydown`, `compat-pending`, `compat-horizontal-keyup`, `compat-vertical-keyup`, `paired-release`, `repeat`, and `reset`.

## Wheel design

Wheel input is normalized separately and then calls the same page-navigation method.

- Positive vertical delta navigates to the next page; negative vertical delta navigates to the previous page.
- Small deltas accumulate so high-resolution touchpads do not feel unresponsive.
- Navigation occurs only after the signed accumulated delta reaches a threshold.
- After navigation, the accumulator resets and a short cooldown suppresses momentum events from the same physical gesture.
- A direction reversal clears the previous accumulation before accumulating the new direction.
- The listener uses `preventDefault` only when the big-screen preview handles the gesture, preventing the document from scrolling at the same time.
- Diagnostics include raw delta, accumulated delta, decision reason, and carousel index before and after navigation.

## Testing

Unit tests replay:

- normal left and right keydown/keyup pairs;
- keyup-only horizontal input;
- captured `ArrowDown` keydown plus `ArrowLeft` keyup plus `ArrowDown` keyup;
- captured `ArrowUp` keydown plus `ArrowRight` keyup plus `ArrowUp` keyup;
- vertical-only compatibility gestures;
- repeats, blur/reset, and unrelated keys;
- wheel threshold accumulation, direction reversal, cooldown, and post-cooldown navigation.

Browser verification uses the real 9090 preview after portal SSO. It checks exact carousel indices for normal keys, replayed abnormal event sequences, and synthetic wheel gestures. With `visDebug=1`, every handled gesture must show a single before/after transition; without it, navigation must work with zero keyboard or wheel diagnostic entries.

## Scope

Only big-screen preview navigation and its focused utilities/tests change. Data APIs, carousel autoplay, slide content, and editor behavior are out of scope.
