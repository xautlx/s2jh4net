import store from 'src/store'
import util from './util'
import {loginOut} from 'src/actions/auth'
import {Toast} from 'antd-mobile'

//const crypto_enable = false


const isDev = process.env.NODE_ENV !== 'production'
// const host = isDev ? '/api' : util.host + '/api'
const host = util.host + '/api'


class Api {

    /**
     * 发起通用请求
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    request(url, options) {
        url = this._prefixUrl(url)
        url = this._appendGobalSearch(url)

        options = Object.assign({}, {
            mode: 'cors',
        }, options)

        options.headers = Object.assign({
            'Access-Token': store.getState().auth.accessToken,
            'Accept': 'application/json',
            'sign': 'dev'
        }, options.headers)

        return fetch(url, options)
            .then(res => res.json())
            .then(res => {
                if (res.type === 'success') {
                    if (isDev) {
                        let path = url.replace(/([^?]*)\?.*/, '$1')
                        console.groupCollapsed(`[request]: %c${path}`, 'color: green')
                        console.log(res.data)
                        console.groupEnd()
                    }
                    return res
                }

                if (res.status === 401) {
                    store.dispatch(loginOut());
                    Toast.fail('身份失效,请重新登录');
                    throw new Error('验证失败');
                }

                throw new Error(res.message)
            })
    }

    /**
     * 发起post请求
     * @param  {string}
     * @param  {object}
     * @return {promise}
     */
    post(url, params) {
        var body = '';
        if (params) {
            for (var key in params) {
                body += '&' + key + '=' + encodeURIComponent(params[key]);
            }
        }
        if (body) {
            body = body.slice(1);
        }

        //let body = JSON.stringify(params)
        //body = crypto_enable ? crypto.encrypt(body) : body

        return this.request(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            },
            body: body
        })
    }

    /**
     * 发起GET请求
     * @param  {string}
     * @param  {object}
     * @return {promise}
     */
    get(url, params) {
        url = this._appendSearch(url, params);

        //let body = JSON.stringify(params)
        //body = crypto_enable ? crypto.encrypt(body) : body

        return this.request(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            }
        })
    }


    /**
     * 对以{http:}或者{\}开头的url添加前缀
     * @param  {string}
     * @return {string}
     */
    _prefixUrl(url = '') {
        if (!url.match(/^(http:)?\//)) {
            url = host + '/' + url
        }

        return url
    }

    /**
     * 为url添加新的search字符串
     * @param  {string}
     * @param  {object}
     * @return {string}
     */

    _appendSearch(url, searchObj) {
        var separator = ~url.indexOf('?') ? '&' : '?'
        let esc = encodeURIComponent
        let search = Object.keys(searchObj)
            .map(k => esc(k) + '=' + esc(searchObj[k]))
            .join('&')

        return url + separator + search
    }

    /**
     * 补齐公用参数
     * @param  {[type]} url [description]
     * @return {[type]}     [description]
     */
    _appendGobalSearch(url) {
        url = this._appendSearch(url, {
            '_': Date.now(),
        })
        return url
    }
}

export default new Api()