(function(e){function n(n){for(var r,a,c=n[0],i=n[1],l=n[2],f=0,s=[];f<c.length;f++)a=c[f],Object.prototype.hasOwnProperty.call(u,a)&&u[a]&&s.push(u[a][0]),u[a]=0;for(r in i)Object.prototype.hasOwnProperty.call(i,r)&&(e[r]=i[r]);d&&d(n);while(s.length)s.shift()();return o.push.apply(o,l||[]),t()}function t(){for(var e,n=0;n<o.length;n++){for(var t=o[n],r=!0,a=1;a<t.length;a++){var c=t[a];0!==u[c]&&(r=!1)}r&&(o.splice(n--,1),e=i(i.s=t[0]))}return e}var r={},a={app:0},u={app:0},o=[];function c(e){return i.p+"js/"+({}[e]||e)+"."+{"chunk-4bb5976f":"376a1bdc","chunk-63f34120":"31a438d1","chunk-157bdbad":"7933e780","chunk-2d0c4873":"ceb3553a","chunk-2d2178fa":"b68353cd","chunk-70a4f67c":"c8ee13b9","chunk-78449fe4":"19f02c92","chunk-76680e5c":"c0624d3e"}[e]+".js"}function i(n){if(r[n])return r[n].exports;var t=r[n]={i:n,l:!1,exports:{}};return e[n].call(t.exports,t,t.exports,i),t.l=!0,t.exports}i.e=function(e){var n=[],t={"chunk-4bb5976f":1,"chunk-63f34120":1,"chunk-157bdbad":1,"chunk-70a4f67c":1,"chunk-78449fe4":1,"chunk-76680e5c":1};a[e]?n.push(a[e]):0!==a[e]&&t[e]&&n.push(a[e]=new Promise((function(n,t){for(var r="css/"+({}[e]||e)+"."+{"chunk-4bb5976f":"358eaf0e","chunk-63f34120":"47a7fe86","chunk-157bdbad":"b8c22865","chunk-2d0c4873":"31d6cfe0","chunk-2d2178fa":"31d6cfe0","chunk-70a4f67c":"115dd09a","chunk-78449fe4":"176f0114","chunk-76680e5c":"304ee797"}[e]+".css",u=i.p+r,o=document.getElementsByTagName("link"),c=0;c<o.length;c++){var l=o[c],f=l.getAttribute("data-href")||l.getAttribute("href");if("stylesheet"===l.rel&&(f===r||f===u))return n()}var s=document.getElementsByTagName("style");for(c=0;c<s.length;c++){l=s[c],f=l.getAttribute("data-href");if(f===r||f===u)return n()}var d=document.createElement("link");d.rel="stylesheet",d.type="text/css",d.onload=n,d.onerror=function(n){var r=n&&n.target&&n.target.src||u,o=new Error("Loading CSS chunk "+e+" failed.\n("+r+")");o.code="CSS_CHUNK_LOAD_FAILED",o.request=r,delete a[e],d.parentNode.removeChild(d),t(o)},d.href=u;var h=document.getElementsByTagName("head")[0];h.appendChild(d)})).then((function(){a[e]=0})));var r=u[e];if(0!==r)if(r)n.push(r[2]);else{var o=new Promise((function(n,t){r=u[e]=[n,t]}));n.push(r[2]=o);var l,f=document.createElement("script");f.charset="utf-8",f.timeout=120,i.nc&&f.setAttribute("nonce",i.nc),f.src=c(e);var s=new Error;l=function(n){f.onerror=f.onload=null,clearTimeout(d);var t=u[e];if(0!==t){if(t){var r=n&&("load"===n.type?"missing":n.type),a=n&&n.target&&n.target.src;s.message="Loading chunk "+e+" failed.\n("+r+": "+a+")",s.name="ChunkLoadError",s.type=r,s.request=a,t[1](s)}u[e]=void 0}};var d=setTimeout((function(){l({type:"timeout",target:f})}),12e4);f.onerror=f.onload=l,document.head.appendChild(f)}return Promise.all(n)},i.m=e,i.c=r,i.d=function(e,n,t){i.o(e,n)||Object.defineProperty(e,n,{enumerable:!0,get:t})},i.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},i.t=function(e,n){if(1&n&&(e=i(e)),8&n)return e;if(4&n&&"object"===typeof e&&e&&e.__esModule)return e;var t=Object.create(null);if(i.r(t),Object.defineProperty(t,"default",{enumerable:!0,value:e}),2&n&&"string"!=typeof e)for(var r in e)i.d(t,r,function(n){return e[n]}.bind(null,r));return t},i.n=function(e){var n=e&&e.__esModule?function(){return e["default"]}:function(){return e};return i.d(n,"a",n),n},i.o=function(e,n){return Object.prototype.hasOwnProperty.call(e,n)},i.p="",i.oe=function(e){throw console.error(e),e};var l=window["webpackJsonp"]=window["webpackJsonp"]||[],f=l.push.bind(l);l.push=n,l=l.slice();for(var s=0;s<l.length;s++)n(l[s]);var d=f;o.push([0,"chunk-vendors"]),t()})({0:function(e,n,t){e.exports=t("56d7")},"034f":function(e,n,t){"use strict";var r=t("8a23"),a=t.n(r);a.a},"56d7":function(e,n,t){"use strict";t.r(n);t("cadf"),t("551c"),t("f751"),t("097d");var r=t("2b0e"),a=function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("v-app",[t("v-content",[t("router-view")],1)],1)},u=[],o={name:"App",components:{},data:function(){return{}}},c=o,i=(t("034f"),t("2877")),l=t("6544"),f=t.n(l),s=t("7496"),d=t("a75b"),h=Object(i["a"])(c,a,u,!1,null,null,null),p=h.exports;f()(h,{VApp:s["a"],VContent:d["a"]});var v=t("f309");r["a"].use(v["a"]);var b=new v["a"]({}),k=t("8c4f"),g=t("d469"),m=function(){return!0};Object(g["a"])()&&Object(g["c"])(m),r["a"].config.productionTip=!1,r["a"].use(k["a"]);var y=[{path:"/",component:function(e){return Promise.all([t.e("chunk-4bb5976f"),t.e("chunk-63f34120"),t.e("chunk-78449fe4")]).then(function(){var n=[t("9261")];e.apply(null,n)}.bind(this)).catch(t.oe)}},{path:"/home",component:function(e){return Promise.all([t.e("chunk-4bb5976f"),t.e("chunk-63f34120"),t.e("chunk-70a4f67c")]).then(function(){var n=[t("eea6")];e.apply(null,n)}.bind(this)).catch(t.oe)}},{path:"/welcome",component:function(e){return Promise.all([t.e("chunk-4bb5976f"),t.e("chunk-76680e5c")]).then(function(){var n=[t("0462")];e.apply(null,n)}.bind(this)).catch(t.oe)}},{path:"/login",component:function(e){return Promise.all([t.e("chunk-4bb5976f"),t.e("chunk-63f34120"),t.e("chunk-157bdbad"),t.e("chunk-2d2178fa")]).then(function(){var n=[t("c6f7")];e.apply(null,n)}.bind(this)).catch(t.oe)}},{path:"/register",component:function(e){return Promise.all([t.e("chunk-4bb5976f"),t.e("chunk-63f34120"),t.e("chunk-157bdbad"),t.e("chunk-2d0c4873")]).then(function(){var n=[t("3ae4")];e.apply(null,n)}.bind(this)).catch(t.oe)}}],w=new k["a"]({routes:y});new r["a"]({vuetify:b,router:w,render:function(e){return e(p)}}).$mount("#app")},"8a23":function(e,n,t){},d469:function(e,n,t){"use strict";t.d(n,"a",(function(){return o})),t.d(n,"b",(function(){return c})),t.d(n,"c",(function(){return l}));var r=t("7618"),a=t("d225"),u=t("b0b4");function o(){return"ClashX Runtime"===navigator.userAgent}var c=null,i=function(){function e(n){var t=this;Object(a["a"])(this,e),this.instance=null,window.native&&(this.instance=window.native,n(this.instance)),this.initBridge((function(e){t.instance=e,n(e)}))}return Object(u["a"])(e,[{key:"initBridge",value:function(e){if(!o())return e(null);if(window.native)return e(window.native);if(window.WVJBCallbacks)return window.WVJBCallbacks.push(e);window.WVJBCallbacks=[e];var n=document.createElement("iframe");n.style.display="none",n.src="https://__bridge_loaded__",document.documentElement.appendChild(n),setTimeout((function(){return document.documentElement.removeChild(n)}),0)}},{key:"callHandler",value:function(e,n){var t=this;return new Promise((function(r){t.instance.callHandler(e,n,r)}))}},{key:"registerHandler",value:function(e,n){var t=this;return new Promise((function(r){t.instance.registerHandler(e,n,r)}))}},{key:"ping",value:function(){return this.callHandler("ping")}},{key:"readConfigString",value:function(){return this.callHandler("readConfigString")}},{key:"getPasteboard",value:function(){return this.callHandler("getPasteboard")}},{key:"getAPIInfo",value:function(){var e=window.native.getRESTfulApi();return"object"===Object(r["a"])(e)?e:"string"===typeof e?JSON.parse(e):e}},{key:"setPasteboard",value:function(e){return this.callHandler("setPasteboard",e)}},{key:"writeConfigWithString",value:function(e){return this.callHandler("writeConfigWithString",e)}},{key:"setSystemProxy",value:function(e){return this.callHandler("setSystemProxy",e)}},{key:"getStartAtLogin",value:function(){return this.callHandler("getStartAtLogin")}},{key:"getProxyDelay",value:function(e){return this.callHandler("speedTest",e)}},{key:"setStartAtLogin",value:function(e){return this.callHandler("setStartAtLogin",e)}},{key:"isSystemProxySet",value:function(){return this.callHandler("isSystemProxySet")}},{key:"checkEnable",value:function(){return console.log("check vpn..."),window.native.checkEnable()}},{key:"startVpn",value:function(){return console.log("native start..."),window.native.startVpn()}},{key:"stopVpn",value:function(){return console.log("native stop..."),window.native.stopVpn()}}]),e}();function l(e){if(c)return e(c);c=new i(e)}}});