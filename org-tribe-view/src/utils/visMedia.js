function getDomainBase() {
  const config = window._CONFIG || {}
  const domain = config['domianURL'] || ''
  return String(domain).replace(/\/$/, '')
}

function withDomain(path) {
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  const domainBase = getDomainBase()
  return domainBase ? `${domainBase}${normalizedPath}` : normalizedPath
}

export function resolveVisMediaUrl(value) {
  if (!value) {
    return ''
  }

  const rawValue = String(value).trim()
  if (!rawValue) {
    return ''
  }

  if (/^(data:|blob:|https?:)?\/\//i.test(rawValue)) {
    return rawValue
  }

  const normalizedValue = rawValue.replace(/\\/g, '/').replace(/^\.\//, '')
  const sysStaticMarker = 'sys/common/static/'
  const sysStaticIndex = normalizedValue.indexOf(sysStaticMarker)

  if (sysStaticIndex >= 0) {
    return withDomain(`/vis/api/${normalizedValue.slice(sysStaticIndex)}`)
  }

  if (normalizedValue.startsWith('/vis/api/') || normalizedValue.startsWith('vis/api/')) {
    return withDomain(normalizedValue.replace(/^\/?/, '/'))
  }

  if (normalizedValue.startsWith('/sys/common/') || normalizedValue.startsWith('sys/common/')) {
    return withDomain(`/vis/api/${normalizedValue.replace(/^\//, '')}`)
  }

  const visualScreenMarker = '/visualScreen/'
  const visualScreenIndex = normalizedValue.indexOf(visualScreenMarker)
  if (visualScreenIndex >= 0) {
    const relativePath = normalizedValue.slice(visualScreenIndex + visualScreenMarker.length).replace(/^\//, '')
    return withDomain(`/vis/api/sys/common/static/${relativePath}`)
  }

  if (/^[a-zA-Z]:\//.test(normalizedValue) || normalizedValue.startsWith('/home/') || normalizedValue.startsWith('/data/')) {
    return ''
  }

  return withDomain(`/vis/api/sys/common/static/${normalizedValue.replace(/^\//, '')}`)
}