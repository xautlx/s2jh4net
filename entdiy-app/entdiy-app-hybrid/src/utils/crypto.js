import CryptoJS from 'crypto-js'

class Crypto {
  _secret = 'api_ios_encoding_key'

  constructor () {
    let md5str = CryptoJS.MD5(this._secret).toString()
    let key = CryptoJS.enc.Utf8.parse(md5str.substr(0, 16))
    let iv = CryptoJS.enc.Utf8.parse(md5str.substr(16, 16))

    this._key = key
    this._cfg = {
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.ZeroPadding,
      iv,
    }
  }

  encrypt (text) {
    return CryptoJS.AES.encrypt(text, this._key, this._cfg).toString()
  }

  decrypt (text) {
    return CryptoJS.AES.decrypt(text, this._key, this._cfg).toString(CryptoJS.enc.Utf8)
  }
}

export default new Crypto()