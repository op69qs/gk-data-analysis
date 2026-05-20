import { postAction } from '@/api/manage'

export function recordMenuEntry(parameter) {
  return postAction('/sys/biz-audit/menu-entry', parameter)
}
