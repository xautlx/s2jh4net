import {Toast} from 'antd-mobile'

class Util {
    platform = /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent) ? 'ios' : 'android'
    rootFontSize = parseFloat(document.documentElement.style.fontSize)
    navbarHeight = this.rootFontSize * .9
    host = window.apiHost || 'http://192.168.3.251:8080/entdiy'

    // url补齐
    patchUrl(url = '') {
        if (!url) return ''
        return /^(https?:|ftp:)/.test(url) ? url : this.host + url
    }

    // 解析search
    // ?a=b&c=d => { a: b, c: d }
    parseSearch(str = '') {
        let dec = decodeURIComponent
        let obj = {}
        str = str.charAt(0) === '?' ? str.substring(1) : str
        str.split('&').forEach(i => {
            let pair = i.split('=')
            obj[dec(pair[0])] = dec([pair[1]])
        })
        return obj
    }

    // html转text
    html2Text(str) {
        let div = document.createElement('DIV')
        div.innerHTML = str
        str = div.innerText
        div = null
        return str
    }

    //扫描二维码
    scanQrcode(callback) {
        if (window.cordova) {
            window.cordova.plugins.barcodeScanner.scan((result) => {
                    if (!result.cancelled) {
                        callback(result.text);
                    } else {
                        //alert(JSON.stringify(result))
                    }
                }, (error) => {
                    //alert("Scanning failed: " + error);
                    Toast.fail("扫描出错" + error);
                },
                {
                    preferFrontCamera: false, // iOS and Android
                    showFlipCameraButton: false, // iOS and Android
                    showTorchButton: false, // iOS and Android
                    torchOn: false, // Android, launch with the torch switched on (if available)
                    prompt: "将二维码放在扫描区域内", // Android
                    resultDisplayDuration: 500, // Android, display scanned text for X ms. 0 suppresses it entirely, default 1500
                    formats: "QR_CODE", // default: all but PDF_417 and RSS_EXPANDED
                    orientation: "portrait", // Android only (portrait|landscape), default unset so it rotates with the device
                    disableAnimations: true, // iOS
                    disableSuccessBeep: false // iOS
                }
            );
        } else {
            Toast.info('当前设备不支持cordova');
        }
    }
}

export default new Util()