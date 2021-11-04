const BACKEND_PORT = process.env.BACKEND_PORT === null ? '' : `:${process.env.BACKEND_PORT}`
const BACKEND_URL = `${location.protocol}//${location.hostname}${BACKEND_PORT}`
const FRONTEND_PORT = process.env.FRONTEND_PORT === null ? '' : `:${process.env.FRONTEND_PORT}`
const REDIRECT_URI = `${location.protocol}//${location.hostname}${FRONTEND_PORT}/oauth/redirect`

export default {
  getSocialLoginUrl (socialType) {
      console.log(`${BACKEND_URL}/oauth2/authorization/${socialType}?redirect_uri=${REDIRECT_URI}`);
    return `${BACKEND_URL}/oauth2/authorization/${socialType}?redirect_uri=${REDIRECT_URI}`
  },
}