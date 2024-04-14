import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import cloudcoverusa2.composeapp.generated.resources.Res
import cloudcoverusa2.composeapp.generated.resources.compose_multiplatform
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

// Proxy for images for web: https://api.allorigins.win/raw?url=

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
//        val state = rememberWebViewState("https://realityexpander.github.io/")
//        val state = rememberWebViewState("https://www.ssec.wisc.edu/data/us_comp/small/").apply {
        val state = rememberWebViewStateWithHTMLData(data = cloudViewPageHtml).apply {
            webSettings.isJavaScriptEnabled = true
        }
        val jsBridge = rememberWebViewJsBridge()


        LaunchedEffect(jsBridge) {
            //jsBridge.dispatch(JsMessage(0, "console.log", "Hello from Compose"))
            //jsBridge.webView?.evaluateJavaScript("console.log('Hello from Compose')")
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    //showContent = !showContent
                    jsBridge.webView?.evaluateJavaScript("\$(\".jsani-stopplay-button\").click()")
                }
            ) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }

            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            WebView(
                state,
                webViewJsBridge = jsBridge,
            )
        }
    }
}

val cloudViewPageHtml = """
    <!DOCTYPE html>
    <html lang="en-US">
        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
            <title>Real-Time U.S. Composite Satellite Imagery: Small Animation &#8212;SSEC
    	</title>
            <meta name="description" content="The Space Science and Engineering Center (SSEC) is an internationally
            			known research center at the University of Wisconsin-Madison.
            			SSEC develops and utilizes instrumentation, algorithms, satellite ground and satellite archive systems
            			to study the Earth and other planetary atmospheres.">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link rel="preload" href="https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/include/css/vendor/uw-rh/redhat-text-latin.v13.woff2" as="font" type="font/woff2" crossorigin>
            <link rel="stylesheet" crossorigin type="text/css" href="https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/include/css/vendor/uw-rh/fonts.0.0.1.min.css"/>
            <!-- Global site tag (gtag.js) - Google Analytics -->
            <script async src="https://www.googletagmanager.com/gtag/js?id=UA-41664135-1"></script>
            <script>
                window.dataLayer = window.dataLayer || [];

                function gtag() {
                    dataLayer.push(arguments);
                }

                gtag('js', new Date());

                gtag('config', 'UA-41664135-1');
            </script>
            <script src="https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/include/js/vendor/jquery-1.12.4.min.js"></script>
            <script src="https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/include/js/layout.js?20170323"></script>
            <script>
                ${'$'}(document).ready(function() {
                    ssec_layout.init();
                });
            </script>
            <link rel="apple-touch-icon" href="https://www.ssec.wisc.edu/apple-touch-icon.png">
            <meta name='robots' content='max-image-preview:large'/>
            <script type="text/javascript">
                /* <![CDATA[ */
                window._wpemojiSettings = {
                    "baseUrl": "https:\/\/s.w.org\/images\/core\/emoji\/15.0.3\/72x72\/",
                    "ext": ".png",
                    "svgUrl": "https:\/\/s.w.org\/images\/core\/emoji\/15.0.3\/svg\/",
                    "svgExt": ".svg",
                    "source": {
                        "concatemoji": "https:\/\/www.ssec.wisc.edu\/wordpress\/wp-includes\/js\/wp-emoji-release.min.js?ver=ff11e7e201b74deb4dd54812d78efdc7"
                    }
                };
                /*! This file is auto-generated */
                !function(i, n) {
                    var o, s, e;
                    function c(e) {
                        try {
                            var t = {
                                supportTests: e,
                                timestamp: (new Date).valueOf()
                            };
                            sessionStorage.setItem(o, JSON.stringify(t))
                        } catch (e) {}
                    }
                    function p(e, t, n) {
                        e.clearRect(0, 0, e.canvas.width, e.canvas.height),
                        e.fillText(t, 0, 0);
                        var t = new Uint32Array(e.getImageData(0, 0, e.canvas.width, e.canvas.height).data)
                          , r = (e.clearRect(0, 0, e.canvas.width, e.canvas.height),
                        e.fillText(n, 0, 0),
                        new Uint32Array(e.getImageData(0, 0, e.canvas.width, e.canvas.height).data));
                        return t.every(function(e, t) {
                            return e === r[t]
                        })
                    }
                    function u(e, t, n) {
                        switch (t) {
                        case "flag":
                            return n(e, "\ud83c\udff3\ufe0f\u200d\u26a7\ufe0f", "\ud83c\udff3\ufe0f\u200b\u26a7\ufe0f") ? !1 : !n(e, "\ud83c\uddfa\ud83c\uddf3", "\ud83c\uddfa\u200b\ud83c\uddf3") && !n(e, "\ud83c\udff4\udb40\udc67\udb40\udc62\udb40\udc65\udb40\udc6e\udb40\udc67\udb40\udc7f", "\ud83c\udff4\u200b\udb40\udc67\u200b\udb40\udc62\u200b\udb40\udc65\u200b\udb40\udc6e\u200b\udb40\udc67\u200b\udb40\udc7f");
                        case "emoji":
                            return !n(e, "\ud83d\udc26\u200d\u2b1b", "\ud83d\udc26\u200b\u2b1b")
                        }
                        return !1
                    }
                    function f(e, t, n) {
                        var r = "undefined" != typeof WorkerGlobalScope && self instanceof WorkerGlobalScope ? new OffscreenCanvas(300,150) : i.createElement("canvas")
                          , a = r.getContext("2d", {
                            willReadFrequently: !0
                        })
                          , o = (a.textBaseline = "top",
                        a.font = "600 32px Arial",
                        {});
                        return e.forEach(function(e) {
                            o[e] = t(a, e, n)
                        }),
                        o
                    }
                    function t(e) {
                        var t = i.createElement("script");
                        t.src = e,
                        t.defer = !0,
                        i.head.appendChild(t)
                    }
                    "undefined" != typeof Promise && (o = "wpEmojiSettingsSupports",
                    s = ["flag", "emoji"],
                    n.supports = {
                        everything: !0,
                        everythingExceptFlag: !0
                    },
                    e = new Promise(function(e) {
                        i.addEventListener("DOMContentLoaded", e, {
                            once: !0
                        })
                    }
                    ),
                    new Promise(function(t) {
                        var n = function() {
                            try {
                                var e = JSON.parse(sessionStorage.getItem(o));
                                if ("object" == typeof e && "number" == typeof e.timestamp && (new Date).valueOf() < e.timestamp + 604800 && "object" == typeof e.supportTests)
                                    return e.supportTests
                            } catch (e) {}
                            return null
                        }();
                        if (!n) {
                            if ("undefined" != typeof Worker && "undefined" != typeof OffscreenCanvas && "undefined" != typeof URL && URL.createObjectURL && "undefined" != typeof Blob)
                                try {
                                    var e = "postMessage(" + f.toString() + "(" + [JSON.stringify(s), u.toString(), p.toString()].join(",") + "));"
                                      , r = new Blob([e],{
                                        type: "text/javascript"
                                    })
                                      , a = new Worker(URL.createObjectURL(r),{
                                        name: "wpTestEmojiSupports"
                                    });
                                    return void (a.onmessage = function(e) {
                                        c(n = e.data),
                                        a.terminate(),
                                        t(n)
                                    }
                                    )
                                } catch (e) {}
                            c(n = f(s, u, p))
                        }
                        t(n)
                    }
                    ).then(function(e) {
                        for (var t in e)
                            n.supports[t] = e[t],
                            n.supports.everything = n.supports.everything && n.supports[t],
                            "flag" !== t && (n.supports.everythingExceptFlag = n.supports.everythingExceptFlag && n.supports[t]);
                        n.supports.everythingExceptFlag = n.supports.everythingExceptFlag && !n.supports.flag,
                        n.DOMReady = !1,
                        n.readyCallback = function() {
                            n.DOMReady = !0
                        }
                    }).then(function() {
                        return e
                    }).then(function() {
                        var e;
                        n.supports.everything || (n.readyCallback(),
                        (e = n.source || {}).concatemoji ? t(e.concatemoji) : e.wpemoji && e.twemoji && (t(e.twemoji),
                        t(e.wpemoji)))
                    }))
                }((window,
                document), window._wpemojiSettings);
                /* ]]> */
            </script>
            <style id='wp-emoji-styles-inline-css' type='text/css'>
                img.wp-smiley, img.emoji {
                    display: inline !important;
                    border: none !important;
                    box-shadow: none !important;
                    height: 1em !important;
                    width: 1em !important;
                    margin: 0 0.07em !important;
                    vertical-align: -0.1em !important;
                    background: none !important;
                    padding: 0 !important;
                }
            </style>
            <link rel='stylesheet' id='wp-block-library-css' href='https://www.ssec.wisc.edu/wordpress/wp-includes/css/dist/block-library/style.min.css?ver=ff11e7e201b74deb4dd54812d78efdc7' type='text/css' media='all'/>
            <style id='classic-theme-styles-inline-css' type='text/css'>
                /*! This file is auto-generated */
                .wp-block-button__link {
                    color: #fff;
                    background-color: #32373c;
                    border-radius: 9999px;
                    box-shadow: none;
                    text-decoration: none;
                    padding: calc(.667em + 2px) calc(1.333em + 2px);
                    font-size: 1.125em
                }

                .wp-block-file__button {
                    background: #32373c;
                    color: #fff;
                    text-decoration: none
                }
            </style>
            <style id='global-styles-inline-css' type='text/css'>
                body {
                    --wp--preset--color--black: #000000;
                    --wp--preset--color--cyan-bluish-gray: #abb8c3;
                    --wp--preset--color--white: #ffffff;
                    --wp--preset--color--pale-pink: #f78da7;
                    --wp--preset--color--vivid-red: #cf2e2e;
                    --wp--preset--color--luminous-vivid-orange: #ff6900;
                    --wp--preset--color--luminous-vivid-amber: #fcb900;
                    --wp--preset--color--light-green-cyan: #7bdcb5;
                    --wp--preset--color--vivid-green-cyan: #00d084;
                    --wp--preset--color--pale-cyan-blue: #8ed1fc;
                    --wp--preset--color--vivid-cyan-blue: #0693e3;
                    --wp--preset--color--vivid-purple: #9b51e0;
                    --wp--preset--gradient--vivid-cyan-blue-to-vivid-purple: linear-gradient(135deg,rgba(6,147,227,1) 0%,rgb(155,81,224) 100%);
                    --wp--preset--gradient--light-green-cyan-to-vivid-green-cyan: linear-gradient(135deg,rgb(122,220,180) 0%,rgb(0,208,130) 100%);
                    --wp--preset--gradient--luminous-vivid-amber-to-luminous-vivid-orange: linear-gradient(135deg,rgba(252,185,0,1) 0%,rgba(255,105,0,1) 100%);
                    --wp--preset--gradient--luminous-vivid-orange-to-vivid-red: linear-gradient(135deg,rgba(255,105,0,1) 0%,rgb(207,46,46) 100%);
                    --wp--preset--gradient--very-light-gray-to-cyan-bluish-gray: linear-gradient(135deg,rgb(238,238,238) 0%,rgb(169,184,195) 100%);
                    --wp--preset--gradient--cool-to-warm-spectrum: linear-gradient(135deg,rgb(74,234,220) 0%,rgb(151,120,209) 20%,rgb(207,42,186) 40%,rgb(238,44,130) 60%,rgb(251,105,98) 80%,rgb(254,248,76) 100%);
                    --wp--preset--gradient--blush-light-purple: linear-gradient(135deg,rgb(255,206,236) 0%,rgb(152,150,240) 100%);
                    --wp--preset--gradient--blush-bordeaux: linear-gradient(135deg,rgb(254,205,165) 0%,rgb(254,45,45) 50%,rgb(107,0,62) 100%);
                    --wp--preset--gradient--luminous-dusk: linear-gradient(135deg,rgb(255,203,112) 0%,rgb(199,81,192) 50%,rgb(65,88,208) 100%);
                    --wp--preset--gradient--pale-ocean: linear-gradient(135deg,rgb(255,245,203) 0%,rgb(182,227,212) 50%,rgb(51,167,181) 100%);
                    --wp--preset--gradient--electric-grass: linear-gradient(135deg,rgb(202,248,128) 0%,rgb(113,206,126) 100%);
                    --wp--preset--gradient--midnight: linear-gradient(135deg,rgb(2,3,129) 0%,rgb(40,116,252) 100%);
                    --wp--preset--font-size--small: 13px;
                    --wp--preset--font-size--medium: 20px;
                    --wp--preset--font-size--large: 36px;
                    --wp--preset--font-size--x-large: 42px;
                    --wp--preset--spacing--20: 0.44rem;
                    --wp--preset--spacing--30: 0.67rem;
                    --wp--preset--spacing--40: 1rem;
                    --wp--preset--spacing--50: 1.5rem;
                    --wp--preset--spacing--60: 2.25rem;
                    --wp--preset--spacing--70: 3.38rem;
                    --wp--preset--spacing--80: 5.06rem;
                    --wp--preset--shadow--natural: 6px 6px 9px rgba(0, 0, 0, 0.2);
                    --wp--preset--shadow--deep: 12px 12px 50px rgba(0, 0, 0, 0.4);
                    --wp--preset--shadow--sharp: 6px 6px 0px rgba(0, 0, 0, 0.2);
                    --wp--preset--shadow--outlined: 6px 6px 0px -3px rgba(255, 255, 255, 1), 6px 6px rgba(0, 0, 0, 1);
                    --wp--preset--shadow--crisp: 6px 6px 0px rgba(0, 0, 0, 1);
                }

                :where(.is-layout-flex) {
                    gap: 0.5em;
                }

                :where(.is-layout-grid) {
                    gap: 0.5em;
                }

                body .is-layout-flow > .alignleft {
                    float: left;
                    margin-inline-start: 0;margin-inline-end: 2em;}

                body .is-layout-flow > .alignright {
                    float: right;
                    margin-inline-start: 2em;margin-inline-end: 0;}

                body .is-layout-flow > .aligncenter {
                    margin-left: auto !important;
                    margin-right: auto !important;
                }

                body .is-layout-constrained > .alignleft {
                    float: left;
                    margin-inline-start: 0;margin-inline-end: 2em;}

                body .is-layout-constrained > .alignright {
                    float: right;
                    margin-inline-start: 2em;margin-inline-end: 0;}

                body .is-layout-constrained > .aligncenter {
                    margin-left: auto !important;
                    margin-right: auto !important;
                }

                body .is-layout-constrained > :where(:not(.alignleft):not(.alignright):not(.alignfull)) {
                    max-width: var(--wp--style--global--content-size);
                    margin-left: auto !important;
                    margin-right: auto !important;
                }

                body .is-layout-constrained > .alignwide {
                    max-width: var(--wp--style--global--wide-size);
                }

                body .is-layout-flex {
                    display: flex;
                }

                body .is-layout-flex {
                    flex-wrap: wrap;
                    align-items: center;
                }

                body .is-layout-flex > * {
                    margin: 0;
                }

                body .is-layout-grid {
                    display: grid;
                }

                body .is-layout-grid > * {
                    margin: 0;
                }

                :where(.wp-block-columns.is-layout-flex) {
                    gap: 2em;
                }

                :where(.wp-block-columns.is-layout-grid) {
                    gap: 2em;
                }

                :where(.wp-block-post-template.is-layout-flex) {
                    gap: 1.25em;
                }

                :where(.wp-block-post-template.is-layout-grid) {
                    gap: 1.25em;
                }

                .has-black-color {
                    color: var(--wp--preset--color--black) !important;
                }

                .has-cyan-bluish-gray-color {
                    color: var(--wp--preset--color--cyan-bluish-gray) !important;
                }

                .has-white-color {
                    color: var(--wp--preset--color--white) !important;
                }

                .has-pale-pink-color {
                    color: var(--wp--preset--color--pale-pink) !important;
                }

                .has-vivid-red-color {
                    color: var(--wp--preset--color--vivid-red) !important;
                }

                .has-luminous-vivid-orange-color {
                    color: var(--wp--preset--color--luminous-vivid-orange) !important;
                }

                .has-luminous-vivid-amber-color {
                    color: var(--wp--preset--color--luminous-vivid-amber) !important;
                }

                .has-light-green-cyan-color {
                    color: var(--wp--preset--color--light-green-cyan) !important;
                }

                .has-vivid-green-cyan-color {
                    color: var(--wp--preset--color--vivid-green-cyan) !important;
                }

                .has-pale-cyan-blue-color {
                    color: var(--wp--preset--color--pale-cyan-blue) !important;
                }

                .has-vivid-cyan-blue-color {
                    color: var(--wp--preset--color--vivid-cyan-blue) !important;
                }

                .has-vivid-purple-color {
                    color: var(--wp--preset--color--vivid-purple) !important;
                }

                .has-black-background-color {
                    background-color: var(--wp--preset--color--black) !important;
                }

                .has-cyan-bluish-gray-background-color {
                    background-color: var(--wp--preset--color--cyan-bluish-gray) !important;
                }

                .has-white-background-color {
                    background-color: var(--wp--preset--color--white) !important;
                }

                .has-pale-pink-background-color {
                    background-color: var(--wp--preset--color--pale-pink) !important;
                }

                .has-vivid-red-background-color {
                    background-color: var(--wp--preset--color--vivid-red) !important;
                }

                .has-luminous-vivid-orange-background-color {
                    background-color: var(--wp--preset--color--luminous-vivid-orange) !important;
                }

                .has-luminous-vivid-amber-background-color {
                    background-color: var(--wp--preset--color--luminous-vivid-amber) !important;
                }

                .has-light-green-cyan-background-color {
                    background-color: var(--wp--preset--color--light-green-cyan) !important;
                }

                .has-vivid-green-cyan-background-color {
                    background-color: var(--wp--preset--color--vivid-green-cyan) !important;
                }

                .has-pale-cyan-blue-background-color {
                    background-color: var(--wp--preset--color--pale-cyan-blue) !important;
                }

                .has-vivid-cyan-blue-background-color {
                    background-color: var(--wp--preset--color--vivid-cyan-blue) !important;
                }

                .has-vivid-purple-background-color {
                    background-color: var(--wp--preset--color--vivid-purple) !important;
                }

                .has-black-border-color {
                    border-color: var(--wp--preset--color--black) !important;
                }

                .has-cyan-bluish-gray-border-color {
                    border-color: var(--wp--preset--color--cyan-bluish-gray) !important;
                }

                .has-white-border-color {
                    border-color: var(--wp--preset--color--white) !important;
                }

                .has-pale-pink-border-color {
                    border-color: var(--wp--preset--color--pale-pink) !important;
                }

                .has-vivid-red-border-color {
                    border-color: var(--wp--preset--color--vivid-red) !important;
                }

                .has-luminous-vivid-orange-border-color {
                    border-color: var(--wp--preset--color--luminous-vivid-orange) !important;
                }

                .has-luminous-vivid-amber-border-color {
                    border-color: var(--wp--preset--color--luminous-vivid-amber) !important;
                }

                .has-light-green-cyan-border-color {
                    border-color: var(--wp--preset--color--light-green-cyan) !important;
                }

                .has-vivid-green-cyan-border-color {
                    border-color: var(--wp--preset--color--vivid-green-cyan) !important;
                }

                .has-pale-cyan-blue-border-color {
                    border-color: var(--wp--preset--color--pale-cyan-blue) !important;
                }

                .has-vivid-cyan-blue-border-color {
                    border-color: var(--wp--preset--color--vivid-cyan-blue) !important;
                }

                .has-vivid-purple-border-color {
                    border-color: var(--wp--preset--color--vivid-purple) !important;
                }

                .has-vivid-cyan-blue-to-vivid-purple-gradient-background {
                    background: var(--wp--preset--gradient--vivid-cyan-blue-to-vivid-purple) !important;
                }

                .has-light-green-cyan-to-vivid-green-cyan-gradient-background {
                    background: var(--wp--preset--gradient--light-green-cyan-to-vivid-green-cyan) !important;
                }

                .has-luminous-vivid-amber-to-luminous-vivid-orange-gradient-background {
                    background: var(--wp--preset--gradient--luminous-vivid-amber-to-luminous-vivid-orange) !important;
                }

                .has-luminous-vivid-orange-to-vivid-red-gradient-background {
                    background: var(--wp--preset--gradient--luminous-vivid-orange-to-vivid-red) !important;
                }

                .has-very-light-gray-to-cyan-bluish-gray-gradient-background {
                    background: var(--wp--preset--gradient--very-light-gray-to-cyan-bluish-gray) !important;
                }

                .has-cool-to-warm-spectrum-gradient-background {
                    background: var(--wp--preset--gradient--cool-to-warm-spectrum) !important;
                }

                .has-blush-light-purple-gradient-background {
                    background: var(--wp--preset--gradient--blush-light-purple) !important;
                }

                .has-blush-bordeaux-gradient-background {
                    background: var(--wp--preset--gradient--blush-bordeaux) !important;
                }

                .has-luminous-dusk-gradient-background {
                    background: var(--wp--preset--gradient--luminous-dusk) !important;
                }

                .has-pale-ocean-gradient-background {
                    background: var(--wp--preset--gradient--pale-ocean) !important;
                }

                .has-electric-grass-gradient-background {
                    background: var(--wp--preset--gradient--electric-grass) !important;
                }

                .has-midnight-gradient-background {
                    background: var(--wp--preset--gradient--midnight) !important;
                }

                .has-small-font-size {
                    font-size: var(--wp--preset--font-size--small) !important;
                }

                .has-medium-font-size {
                    font-size: var(--wp--preset--font-size--medium) !important;
                }

                .has-large-font-size {
                    font-size: var(--wp--preset--font-size--large) !important;
                }

                .has-x-large-font-size {
                    font-size: var(--wp--preset--font-size--x-large) !important;
                }

                .wp-block-navigation a:where(:not(.wp-element-button)) {
                    color: inherit;
                }

                :where(.wp-block-post-template.is-layout-flex) {
                    gap: 1.25em;
                }

                :where(.wp-block-post-template.is-layout-grid) {
                    gap: 1.25em;
                }

                :where(.wp-block-columns.is-layout-flex) {
                    gap: 2em;
                }

                :where(.wp-block-columns.is-layout-grid) {
                    gap: 2em;
                }

                .wp-block-pullquote {
                    font-size: 1.5em;
                    line-height: 1.6;
                }
            </style>
            <link rel='stylesheet' id='block-ssec-gallery_front-css' href='https://www.ssec.wisc.edu/wordpress/wp-content/plugins/ssec-gallery-block/src/../css/style.min.css?ver=20230105' type='text/css' media='all'/>
            <link rel='stylesheet' id='ssec-hanis-style-css' href='https://www.ssec.wisc.edu/wordpress/wp-content/plugins/ssec-hanis/style.min.css?ver=1.3' type='text/css' media='all'/>
            <link rel='stylesheet' id='block-ssec-random-image_front-css' href='https://www.ssec.wisc.edu/wordpress/wp-content/plugins/ssec-random-image/src/../css/style.min.css?ver=ff11e7e201b74deb4dd54812d78efdc7' type='text/css' media='all'/>
            <link rel='stylesheet' id='ssec_main-style-css' href='https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/style.min.css?ver=3.3' type='text/css' media='all'/>
            <link rel="https://api.w.org/" href="https://www.ssec.wisc.edu/wp-json/"/>
            <link rel="alternate" type="application/json" href="https://www.ssec.wisc.edu/wp-json/wp/v2/pages/601"/>
            <link rel="EditURI" type="application/rsd+xml" title="RSD" href="https://www.ssec.wisc.edu/wordpress/xmlrpc.php?rsd"/>
            <link rel="canonical" href="https://www.ssec.wisc.edu/data/us_comp/small/"/>
            <link rel='shortlink' href='https://www.ssec.wisc.edu/?p=601'/>
            <link rel="alternate" type="application/json+oembed" href="https://www.ssec.wisc.edu/wp-json/oembed/1.0/embed?url=https%3A%2F%2Fwww.ssec.wisc.edu%2Fdata%2Fus_comp%2Fsmall%2F"/>
            <link rel="alternate" type="text/xml+oembed" href="https://www.ssec.wisc.edu/wp-json/oembed/1.0/embed?url=https%3A%2F%2Fwww.ssec.wisc.edu%2Fdata%2Fus_comp%2Fsmall%2F&#038;format=xml"/>
            <script src="/wordpress/wp-content/themes/ssec_main/include/js/vendor/jquery.jsani-2.95.beta/jquery.jsani-loader.min.js"></script>
            <script>
                jQuery(document).ready(function(${'$'}) {
                    ${'$'}('a input[type=button]').bind('click', function(e) {
                        window.location.href = this.parentNode.getAttribute('href');
                        return false;
                        // stop hyperlink from working so that url isn't requested twice
                    });
                    var animation_1 = ${'$'}("#jsani").jsani({
                        baseDir: '/data/us_comp',
                        imageSrcs: ['image0.jpg', 'image1.jpg', 'image2.jpg', 'image3.jpg', 'image4.jpg', 'image5.jpg', 'image6.jpg', 'image7.jpg'],
                        aniWidth: 800,
                        aniHeight: 600,
                        initdwell: 200,
                        controls: ['stopplay', 'firstframe', 'previous', 'next', 'lastframe', 'looprock', 'slow', 'fast', 'zoom'],
                        last_frame_pause: 4
                    });
                });
            </script>
            <!-- IMPROVE: should this be placed above wp_head so that wp_head is the last thing in the header?
    	This is how the starter kit was constructed but this seems like a mistake to me -->
        </head>
        <body class="page-template-default page page-id-601 page-child parent-pageid-592" data-template="base.twig">
            <div class="sf-wrapper ">
                <div id="header">
                    <div id="nav-other">
                        <ul>
                            <li>
                                <a href="https://www.ssec.wisc.edu/researchsupport/">Research Support</a>
                            </li>
                            <li>
                                <a href="https://www.wisc.edu/">UW</a>
                            </li>
                            <li>
                                <a href="https://cimss.ssec.wisc.edu/aspb/">NOAA ASPB</a>
                            </li>
                            <li>
                                <a href="https://cimss.ssec.wisc.edu/">CIMSS</a>
                            </li>
                            <li>
                                <a href="https://www.ssec.wisc.edu/datacenter/">Data Services</a>
                            </li>
                            <li>
                                <a href="https://library.ssec.wisc.edu/">Library</a>
                            </li>
                            <li class="search-container">
                                <form role="search" method="get" class="searchform" action="/search/">
                                    <div class="inner-addon right-addon">
                                        <label class="screen-reader-text" for="q2">Search for:</label>
                                        <span class="glyphicon glyphicon-search">
                                            <svg aria-hidden="true" class="icon" id="search-icon" viewBox="0 75 1792 1792">
                                                <title>Search Icon</title>
                                                <path fill="inherit" d="M1216 832q0-185-131.5-316.5t-316.5-131.5-316.5 131.5-131.5 316.5 131.5 316.5 316.5 131.5 316.5-131.5 131.5-316.5zm512 832q0 52-38 90t-90 38q-54 0-90-38l-343-342q-179 124-399 124-143 0-273.5-55.5t-225-150-150-225-55.5-273.5 55.5-273.5 150-225 225-150 273.5-55.5 273.5 55.5 225 150 150 225 55.5 273.5q0 220-124 399l343 343q37 37 37 90z"/>
                                            </svg>
                                        </span>
                                        <input class="form-control search-box" value="" placeholder="Search" id="q2" name="q" type="text">
                                    </div>
                                </form>
                            </li>
                        </ul>
                    </div>
                    <div id="mobile-menu-search">
                        <div class="wrapper">
                            <img src="https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/include/img/search-icon-large.png" id="search-icon-mobile" alt="">
                            <button id="mobile-menu-button">
                                <span id="mobile-menu-button-text">MENU</span>
                                <span id="mobile-menu-button-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="83" height="50" viewBox="0 0 83 50" aria-hidden="true">
                                        <defs>
                                            <style>
                                                .cls-1 {
                                                    fill: #214184;
                                                }
                                            </style>
                                        </defs>
                                        <rect id="Rounded_Rectangle_1" data-name="Rounded Rectangle 1" class="cls-1" x="1" y="1" width="81" height="9" rx="4.5" ry="4.5" aria-hidden="true" role="presentation"/>
                                        <rect id="Rounded_Rectangle_1_copy" data-name="Rounded Rectangle 1 copy" class="cls-1" x="1" y="20" width="81" height="9" rx="4.5" ry="4.5" aria-hidden="true" role="presentation"/>
                                        <rect id="Rounded_Rectangle_1_copy_2" data-name="Rounded Rectangle 1 copy 2" class="cls-1" x="1" y="40" width="81" height="9" rx="4.5" ry="4.5" aria-hidden="true" role="presentation"/>
                                    </svg>
                                </span>
                            </button>
                        </div>
                        <div class="wrapper" id="mobile-search-container">
                            <form role="search" method="get" class="searchform" action="/search/" style="width: 100%;">
                                <div class="inner-addon right-addon">
                                    <label class="screen-reader-text" for="q">Search for:</label>
                                    <input class="form-control search-box" value="" placeholder="search" id="q" name="q" type="text">
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="wrapper">
                        <h1>
                            <span id="ssec-logo-container">
                                <a href="/">
                                    <img src="https://www.ssec.wisc.edu/wordpress/wp-content/themes/ssec_main/include/img/ssec-logo.png" alt="SSEC Home" id="ssec-logo">
                                </a>
                            </span>
                            <span id="title-container">
                                <a href="/">
                                    <span id="main-title">Space Science and Engineering Center</span>
                                    <span id="sub-title">University of Wisconsin-Madison</span>
                                </a>
                            </span>
                        </h1>
                    </div>
                    <div id="nav-main" class="nav-menu ">
                        <ul class="wrapper ">
                            <li class="">
                                <a href="https://www.ssec.wisc.edu/research/">Research</a>
                            </li>
                            <li class="current_nav_link">
                                <a href="https://www.ssec.wisc.edu/data/">Data/Imagery</a>
                            </li>
                            <li class="">
                                <a href="https://www.ssec.wisc.edu/software/">Software</a>
                            </li>
                            <li class="">
                                <a href="/news/">News</a>
                            </li>
                            <li class="">
                                <a href="https://www.ssec.wisc.edu/education/">Education</a>
                            </li>
                            <li class="">
                                <a href="https://www.ssec.wisc.edu/about/">About</a>
                            </li>
                            <li class="nav-other-mobile-only" id="nav-other-mobile-only-top-border"></li>
                            <li class="nav-other-mobile-only">
                                <a href="https://www.ssec.wisc.edu/researchsupport/">Research Support</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="https://www.wisc.edu/">UW</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="https://cimss.ssec.wisc.edu/aspb/">NOAA ASPB</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="https://cimss.ssec.wisc.edu/">CIMSS</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="https://www.ssec.wisc.edu/datacenter/">Data Services</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="https://library.ssec.wisc.edu/">AOSS Library</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="/partners">Partners</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="/employment">Careers</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="/disclaimer">Disclaimer</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="/staff/">Staff Directory</a>
                            </li>
                            <li class="nav-other-mobile-only">
                                <a href="https://groups.ssec.wisc.edu/">For Employees</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="main">
                    <div class="wrapper secondary-page-content">
                        <h2>Real-Time U.S. Composite Satellite Imagery: Small Animation</h2>
                        <div class="column-container">
                            <div class="content-column">
                                <div style="text-align: center;">
                                    <div id="us_nav">
                                        <a href="/data/us_comp">
                                            <input value="Latest Image" type="button"></input>
                                        </a>
                                        <input value="Small version" disabled="" type="button"></input>
                                        <a href="/data/us_comp/large">
                                            <input value="Large version" type="button"></input>
                                        </a>
                                        <a href="/data/us_comp/movie">
                                            <input value="Large version - 7 Day HTML5 Movie" type="button"></input>
                                        </a>
                                    </div>
                                    <div id="jsani"></div>
                                    <p style="margin-top: 5px;">
                                        <a href="/data/us_comp/info">About these images</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <span id="mobile_test"></span>
                </div>
                <div class="sf-push "></div>
            </div>
            <div id="footer" class="sf-footer ">
                <div class="wrapper">
                    <div id="footer-container-1">
                        <a href="http://www.wisc.edu/" id="uw-logo" aria-label="University of Wisconsin">
                            <svg id="uw-logo-svg" data-name="UW logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 326 216" aria-hidden="true" role="presentation">
                                <title>UW Logo</title>
                                <g>
                                    <path d="M21.3,195.1c-5.7-4.1-6.2-3.7-8.3-10.1l-7.3-23c-1.1-3.4-2.1-7.5-5.2-9.6v-0.2h9.1c-0.2,1.4,0.4,3.3,0.7,4.5l9.3,28.9
              l7.3-23.7c1.2-3.8,2.4-7.8-1-9.6v-0.2h8.8c-0.4,1.6,0.1,3.5,0.9,6.3l8.7,27l7-22.8c1.1-3.5,3.2-8.6-1.7-10.3v-0.2h9.6l-13.1,42.8
              c-5.7-3.9-6.2-3.6-8.2-10L31,163.7L21.3,195.1z"/>
                                    <path d="M64.5,161.1c0-2.9,0.4-7.4-2.7-8.6v-0.2h11.8v0.2c-3.1,1.2-2.7,5.8-2.7,8.6v23.5c0,2.8-0.4,7.3,2.7,8.5v0.2H61.7v-0.2
              c3.1-1.1,2.7-5.7,2.7-8.5V161.1z"/>
                                    <path d="M102.2,159.7c-2.4-2.5-6.3-3.9-9.8-3.9c-3.5,0-8,1.4-8,5.7c0,8.5,21.4,8.4,21.4,19.9c0,6.7-7,12.9-16.7,12.9
              c-3.8,0-7.6-0.6-11.1-1.9l-1.8-7.8c3.7,3.2,8.8,5.2,13.6,5.2c3.5,0,8.9-2.1,8.9-6.5c0-9.7-21.4-8-21.4-21.2c0-7.8,8-10.8,15.6-10.8
              c3.1,0,6.3,0.4,9.3,1.4V159.7z"/>
                                    <path d="M141.5,160c-3.4-2.2-7.4-3.5-11.5-3.5c-8.6,0-14.6,6.3-14.6,15.3c0,9.2,6.2,17.3,15.8,17.3c4.7,0,9.3-1.7,13.3-3.9h0.2
              l-3.2,7.2c-2.9,1.4-6.2,1.9-9.4,1.9c-14.7,0-23.8-8.2-23.8-21.5c0-12.9,9.1-21.4,21.8-21.4c3.8,0,7.7,0.7,11.3,1.6V160z"/>
                                    <path d="M189.9,172.3c0,12.7-10.5,21.9-22.9,21.9c-12.3,0-22.7-8.8-22.7-21.6c0-11.8,10.3-21.7,23.3-21.4
              C180.7,151.2,189.9,161.1,189.9,172.3z M151.3,172.1c0,8.7,6.2,17.7,15.9,17.7c9.5,0,15.5-6.9,15.5-16.2c0-9.1-5.8-17.9-15.6-17.9
              C157.5,155.7,151.3,162.7,151.3,172.1z"/>
                                    <path d="M199.3,185.2c0,3.5-0.1,6,3.2,7.9v0.2h-11v-0.2c3.3-1.9,3.3-4.4,3.3-7.9v-24.8c0-3.5,0-6-3.2-7.8v-0.2h9.1v0.1
              c0.2,0.7,0.5,1.1,0.9,1.6l0.9,1.2l22.8,28.8v-23.6c0-3.5,0.1-6-3.2-7.8v-0.2h11v0.2c-3.2,1.9-3.2,4.4-3.2,7.8v34.9
              c-4.4-1.5-6.4-3.5-9.1-7.1l-21.4-27.1V185.2z"/>
                                    <path d="M261.2,159.7c-2.4-2.5-6.3-3.9-9.8-3.9c-3.5,0-8,1.4-8,5.7c0,8.5,21.4,8.4,21.4,19.9c0,6.7-7,12.9-16.7,12.9
              c-3.8,0-7.6-0.6-11.1-1.9l-1.8-7.8c3.7,3.2,8.8,5.2,13.6,5.2c3.5,0,8.9-2.1,8.9-6.5c0-9.7-21.4-8-21.4-21.2c0-7.8,8-10.8,15.6-10.8
              c3.1,0,6.3,0.4,9.3,1.4V159.7z"/>
                                    <path d="M270.7,161.1c0-2.9,0.4-7.4-2.7-8.6v-0.2h11.8v0.2c-3.1,1.2-2.7,5.8-2.7,8.6v23.5c0,2.8-0.4,7.3,2.7,8.5v0.2H268v-0.2
              c3.1-1.1,2.7-5.7,2.7-8.5V161.1z"/>
                                    <path d="M291.8,185.2c0,3.5-0.1,6,3.2,7.9v0.2h-11v-0.2c3.3-1.9,3.3-4.4,3.3-7.9v-24.8c0-3.5,0-6-3.2-7.8v-0.2h9.1v0.1
              c0.2,0.7,0.5,1.1,0.9,1.6l0.9,1.2l22.8,28.8v-23.6c0-3.5,0.1-6-3.2-7.8v-0.2h11v0.2c-3.2,1.9-3.2,4.4-3.2,7.8v34.9
              c-4.4-1.5-6.4-3.5-9.1-7.1l-21.4-27.1V185.2z"/>
                                </g>
                                <g>
                                    <path d="M11.7,203.2v7c0,2.7,1.5,3.5,3,3.5c1.7,0,3-0.8,3-3.5v-7h1.5v6.9c0,3.8-2.1,4.8-4.5,4.8c-2.4,0-4.5-1.2-4.5-4.7v-7H11.7z"/>
                                    <path d="M22.3,214.8v-11.6h2c1.7,2.7,5.3,8.5,5.9,9.7h0c-0.1-1.5-0.1-3.3-0.1-5.2v-4.5h1.4v11.6h-1.9c-1.5-2.5-5.3-8.7-6-10h0
              c0.1,1.3,0.1,3.3,0.1,5.4v4.6H22.3z"/>
                                    <path d="M36.6,203.2v11.6h-1.5v-11.6H36.6z"/>
                                    <path d="M42.9,214.8l-4.2-11.6h1.6l2,5.8c0.6,1.7,1.2,3.4,1.4,4.5h0c0.2-0.9,0.9-2.7,1.5-4.4l2.1-5.8H49l-4.4,11.6H42.9z"/>
                                    <path d="M58.4,209.3h-5.7v4.1H59l-0.2,1.3h-7.6v-11.6h7.5v1.3h-6v3.5h5.7V209.3z"/>
                                    <path d="M63,209.7v5.1h-1.5v-11.6H66c2.4,0,3.6,1.3,3.6,3.1c0,1.5-0.9,2.4-2,2.7c0.9,0.2,1.8,1,1.8,3v0.5c0,0.8,0,1.8,0.2,2.3h-1.5
              c-0.2-0.5-0.2-1.4-0.2-2.5V212c0-1.6-0.5-2.4-2.5-2.4H63z M63,208.4h2.5c1.8,0,2.5-0.7,2.5-2c0-1.2-0.8-1.9-2.4-1.9H63V208.4z"/>
                                    <path d="M73.3,211.5c0.3,1.4,1.3,2.1,2.9,2.1c1.8,0,2.5-0.8,2.5-1.9c0-1.2-0.6-1.8-2.9-2.4c-2.7-0.7-3.7-1.6-3.7-3.2
              c0-1.7,1.3-3.1,3.8-3.1c2.8,0,3.9,1.6,4.1,3.1h-1.6c-0.2-1-0.8-1.9-2.5-1.9c-1.4,0-2.2,0.6-2.2,1.7c0,1.1,0.6,1.5,2.7,2
              c3.3,0.8,3.9,2.1,3.9,3.6c0,1.9-1.4,3.3-4.2,3.3c-2.9,0-4.2-1.6-4.4-3.4H73.3z"/>
                                    <path d="M84.5,203.2v11.6H83v-11.6H84.5z"/>
                                    <path d="M90.4,204.5h-3.7v-1.3h9v1.3h-3.7v10.3h-1.5V204.5z"/>
                                    <path d="M101.2,214.8v-4.2c0-0.1,0-0.2-0.1-0.3l-4.1-7h1.8c1,1.9,2.7,4.9,3.3,5.9c0.5-1,2.2-4,3.3-5.9h1.6l-4.2,7
              c0,0.1-0.1,0.1-0.1,0.3v4.2H101.2z"/>
                                    <path d="M123.5,208.9c0,3.2-1.8,6-5.4,6c-3.4,0-5.3-2.6-5.3-5.9c0-3.3,1.9-5.9,5.4-5.9C121.5,203,123.5,205.5,123.5,208.9z
               M114.4,208.9c0,2.5,1.3,4.7,3.8,4.7c2.7,0,3.8-2.2,3.8-4.7c0-2.5-1.2-4.6-3.8-4.6C115.5,204.3,114.4,206.5,114.4,208.9z"/>
                                    <path d="M126.3,203.2h7.4v1.3h-5.9v3.8h5.5v1.3h-5.5v5.2h-1.5V203.2z"/>
                                    <path d="M142.5,214.8l-3.1-11.6h1.6c0.9,3.8,2.1,8.6,2.4,9.9h0c0.3-1.5,1.9-6.9,2.6-9.9h1.5c0.7,2.6,2.3,8.5,2.5,9.8h0
              c0.4-2,2-7.5,2.6-9.8h1.5l-3.4,11.6h-1.6c-0.7-2.7-2.2-8.2-2.5-9.6h0c-0.3,1.7-1.8,6.9-2.6,9.6H142.5z"/>
                                    <path d="M158,203.2v11.6h-1.5v-11.6H158z"/>
                                    <path d="M162.2,211.5c0.3,1.4,1.3,2.1,2.9,2.1c1.8,0,2.5-0.8,2.5-1.9c0-1.2-0.6-1.8-2.9-2.4c-2.7-0.7-3.7-1.6-3.7-3.2
              c0-1.7,1.3-3.1,3.8-3.1c2.8,0,3.9,1.6,4.1,3.1h-1.6c-0.2-1-0.8-1.9-2.5-1.9c-1.4,0-2.2,0.6-2.2,1.7c0,1.1,0.6,1.5,2.7,2
              c3.3,0.8,3.9,2.1,3.9,3.6c0,1.9-1.4,3.3-4.2,3.3c-2.9,0-4.2-1.6-4.4-3.4H162.2z"/>
                                    <path d="M181.1,211.5c-0.5,2-2,3.4-4.6,3.4c-3.5,0-5.2-2.6-5.2-5.9c0-3.1,1.7-6,5.3-6c2.7,0,4.2,1.6,4.6,3.5h-1.5
              c-0.4-1.3-1.2-2.2-3.1-2.2c-2.6,0-3.6,2.3-3.6,4.7c0,2.3,1,4.7,3.7,4.7c1.8,0,2.6-1,3-2.1H181.1z"/>
                                    <path d="M193.9,208.9c0,3.2-1.8,6-5.4,6c-3.4,0-5.3-2.6-5.3-5.9c0-3.3,1.9-5.9,5.4-5.9C191.8,203,193.9,205.5,193.9,208.9z
               M184.7,208.9c0,2.5,1.3,4.7,3.8,4.7c2.7,0,3.8-2.2,3.8-4.7c0-2.5-1.2-4.6-3.8-4.6C185.9,204.3,184.7,206.5,184.7,208.9z"/>
                                    <path d="M196.6,214.8v-11.6h2c1.7,2.7,5.3,8.5,5.9,9.7h0c-0.1-1.5-0.1-3.3-0.1-5.2v-4.5h1.4v11.6H204c-1.5-2.5-5.3-8.7-6-10h0
              c0.1,1.3,0.1,3.3,0.1,5.4v4.6H196.6z"/>
                                    <path d="M210.1,211.5c0.3,1.4,1.3,2.1,2.9,2.1c1.8,0,2.5-0.8,2.5-1.9c0-1.2-0.6-1.8-2.9-2.4c-2.7-0.7-3.7-1.6-3.7-3.2
              c0-1.7,1.3-3.1,3.8-3.1c2.8,0,3.9,1.6,4.1,3.1h-1.6c-0.2-1-0.8-1.9-2.5-1.9c-1.4,0-2.2,0.6-2.2,1.7c0,1.1,0.6,1.5,2.7,2
              c3.3,0.8,3.9,2.1,3.9,3.6c0,1.9-1.4,3.3-4.2,3.3c-2.9,0-4.2-1.6-4.4-3.4H210.1z"/>
                                    <path d="M221.3,203.2v11.6h-1.5v-11.6H221.3z"/>
                                    <path d="M224.8,214.8v-11.6h2c1.7,2.7,5.3,8.5,5.9,9.7h0c-0.1-1.5-0.1-3.3-0.1-5.2v-4.5h1.4v11.6h-1.9c-1.5-2.5-5.3-8.7-6-10h0
              c0.1,1.3,0.1,3.3,0.1,5.4v4.6H224.8z"/>
                                    <path d="M243.4,209.8v1.3h-6.6v-1.3H243.4z"/>
                                    <path d="M256.1,209.8c0-2.2,0-4.3,0.1-5.6h-0.1c-0.5,1.9-2.4,6.8-3.7,10.5H251c-1-3-2.9-8.5-3.5-10.5h0c0.1,1.3,0.1,3.9,0.1,5.9
              v4.6h-1.4v-11.6h2.3c1.3,3.6,2.9,8.2,3.3,9.7h0c0.3-1.1,2.2-6.2,3.5-9.7h2.2v11.6h-1.5V209.8z"/>
                                    <path d="M262.6,211.3l-1.2,3.5h-1.5l4.1-11.6h1.9l4.3,11.6h-1.6l-1.3-3.5H262.6z M266.8,210c-1.1-3-1.7-4.7-2-5.5h0
              c-0.3,1-1,2.9-1.8,5.5H266.8z"/>
                                    <path d="M272.3,203.2h4.1c3.3,0,5.5,2.2,5.5,5.7c0,3.4-2.1,5.9-5.5,5.9h-4.1V203.2z M273.8,213.5h2.4c2.7,0,4-2,4-4.6
              c0-2.3-1.2-4.4-4-4.4h-2.4V213.5z"/>
                                    <path d="M286.2,203.2v11.6h-1.5v-11.6H286.2z"/>
                                    <path d="M290.3,211.5c0.3,1.4,1.3,2.1,2.9,2.1c1.8,0,2.5-0.8,2.5-1.9c0-1.2-0.6-1.8-2.9-2.4c-2.7-0.7-3.7-1.6-3.7-3.2
              c0-1.7,1.3-3.1,3.8-3.1c2.8,0,3.9,1.6,4.1,3.1h-1.6c-0.2-1-0.8-1.9-2.5-1.9c-1.4,0-2.2,0.6-2.2,1.7c0,1.1,0.6,1.5,2.7,2
              c3.3,0.8,3.9,2.1,3.9,3.6c0,1.9-1.4,3.3-4.2,3.3c-2.9,0-4.2-1.6-4.4-3.4H290.3z"/>
                                    <path d="M310.1,208.9c0,3.2-1.8,6-5.4,6c-3.4,0-5.3-2.6-5.3-5.9c0-3.3,1.9-5.9,5.4-5.9C308.1,203,310.1,205.5,310.1,208.9z
               M301,208.9c0,2.5,1.3,4.7,3.8,4.7c2.7,0,3.8-2.2,3.8-4.7c0-2.5-1.2-4.6-3.8-4.6C302.2,204.3,301,206.5,301,208.9z"/>
                                    <path d="M312.9,214.8v-11.6h2c1.7,2.7,5.3,8.5,5.9,9.7h0c-0.1-1.5-0.1-3.3-0.1-5.2v-4.5h1.4v11.6h-1.9c-1.5-2.5-5.3-8.7-6-10h0
              c0.1,1.3,0.1,3.3,0.1,5.4v4.6H312.9z"/>
                                </g>
                                <path d="M211.4,52c-0.3-3-0.8-6-1.4-8.9c-0.6-2.7-1.3-5.4-2.3-8c-1-2.8-2.2-5.5-3.6-8c-1.4-2.4-3-4.6-4.8-6.7
            c-4.9-5.4-11.1-8.4-17.8-8.5l-0.1,0c-1.1-2.4-4.1-4.4-6.8-4.7c0,0-1.4-6-7.8-6.1c-6.4,0-7.8,6.1-7.8,6.1c-2.6,0.4-5.7,2.3-6.8,4.7
            l-0.1,0c-6.8,0.1-12.9,3.1-17.8,8.5c-1.9,2-3.4,4.3-4.8,6.7c-1.4,2.5-2.6,5.3-3.6,8c-0.9,2.6-1.7,5.3-2.3,8
            c-0.6,2.9-1.1,5.9-1.4,8.9c-3.7,36.8,12.8,82,42.5,88.9c0.5,0.7,1.5,2.1,2.1,2.1c0.6,0,1.6-1.4,2.1-2.1
            C198.6,134,215.1,88.8,211.4,52z M166.8,2.5c5.2,0,6.4,4.4,6.4,4.5c-2.6,0.2-5,1.7-6.3,4c0,0-0.1,0.2-0.1,0.2
            c-0.1,0-0.1-0.1-0.1-0.2c-1.3-2.3-3.7-3.8-6.3-4C160.4,6.9,161.5,2.5,166.8,2.5z M163.7,9.5c1.3,0.8,2.2,2,2.6,3.4
            c0.1,0.4,0.3,0.7,0.4,0.7s0.3-0.3,0.4-0.7c0.4-1.4,1.3-2.7,2.6-3.4c3.8-2.3,8.1-0.8,9.8,2.6c-0.9,0.1-1.7,0.4-2.7,0.7
            c-1.5,0.5-2.8,1.2-3.9,2.1c-2.4,2.2-4.5,5.6-4.9,9.9c-1.1,11.9,12.6,10,13.2,10c1.8,0,3.1-2.8,3.1-6.5c0-3.7-1.4-6.5-3.1-6.5
            c-1.1,0-2.9,0.8-3.1,4.1c-0.1,2.3,1,5.4,2.5,5.6c0.8,0.1,1.5-1.2,1.7-2.6c0.2-1.2,0.1-3-1-3.3c-0.3-0.1-0.7,0.1-0.8,0.5
            c-0.1,0.3,0.7,0.8,0.6,2.2c0,2-2.1,1-1.8-2.2c0.2-2.9,1.7-2.7,1.8-2.7c0.8,0,2.1,1.7,2.1,4.9c0,2.8-1.4,5.9-4,4.3
            c-2.2-1.6-3.1-5.3-2.5-8.2c0.3-1.3,1.4-4.9,5.1-4.9c5.3,0,10.2,2.8,14.2,8c-0.4,1.4-3.2,6.7-11.8,11.7c-0.2,0.1-0.5,0.3-0.5,0.2
            c-2.6-1.1-5.1-1.8-8-1.6c-2.6,0.1-5.1,0.6-7.5,1.2c-0.5,0.1-1,0.2-1.5,0.2c-0.5,0-1-0.1-1.5-0.2c-2.5-0.6-5-1.1-7.5-1.2
            c-2.9-0.2-5.4,0.5-8,1.6c0,0-0.3-0.2-0.5-0.2c-8.7-5.1-11.4-10.3-11.8-11.7c3.9-5.2,8.9-8,14.2-8c3.7,0,4.8,3.6,5.1,4.9
            c0.6,2.9-0.3,6.6-2.5,8.2c-2.6,1.6-4-1.5-4-4.3c0-3.2,1.2-4.9,2.1-4.9c0.1,0,1.5-0.2,1.8,2.7c0.3,3.2-1.8,4.2-1.8,2.2
            c0-1.4,0.7-1.9,0.6-2.2c-0.1-0.3-0.4-0.5-0.8-0.5c-1.1,0.3-1.2,2.1-1,3.3c0.2,1.4,0.9,2.7,1.7,2.6c1.5-0.2,2.6-3.3,2.5-5.6
            c-0.2-3.3-2-4.1-3.1-4.1c-1.7,0-3.1,2.8-3.1,6.5c0,3.7,1.3,6.5,3.1,6.5c0.7,0.1,14.3,1.9,13.2-10c-0.4-4.3-2.5-7.7-4.9-9.9
            c-1.1-1-2.3-1.6-3.9-2.1c-1.1-0.3-1.8-0.5-2.7-0.7C155.6,8.7,159.9,7.1,163.7,9.5z M138.1,58.5c-4.4-0.4-7.8-1.9-9.4-2.7
            c0.7-11.9,3.7-21.2,8.1-27.4c0.8,2,4.2,7.1,12,11.7C142.1,44,139.1,52.3,138.1,58.5z M149.6,41.4c2-1.2,4.5-1.8,7.1-2
            c4.5-0.3,7.8,1.4,10,1.5h0.1c2.2-0.1,5.5-1.8,10-1.5c2.6,0.2,5.2,0.7,7.1,2c9.1,5.8,10.4,19.1,10.5,23.5c0.6,15.5-4.2,27.5-5.8,30.9
            c-6.3,13.8-13.9,23.6-21.4,26.9l-0.6,0.3l-0.6-0.3c-7.5-3.3-15.1-13.1-21.4-26.9c-1.5-3.3-6.4-15.4-5.8-30.9
            C139.2,60.6,140.5,47.2,149.6,41.4z M137.9,59.3c-0.3,2.3-0.5,4.2-0.5,5.4c-0.2,4.3,0,8.6,0.6,12.9c0.6,4,1.5,8,2.7,11.9
            c1.7,5.3,4,10.3,6.7,15.2c-2.8,2.5-7,3.5-8.4,3.8c-7.5-15-11.4-34.3-10.5-51.7C130.4,57.6,133.7,59,137.9,59.3z M147.9,105.4
            c5,8.6,10,13.8,13.7,16.5c-1.4,3.5-4.6,6.2-5.8,7.1c-6.5-4.4-12-11.3-16.4-19.7C141.2,108.9,145.1,107.9,147.9,105.4z M162.4,122.4
            c2,1.5,4.1,2.3,4.3,2.4c0.3-0.1,2.3-0.9,4.3-2.4c1.4,3.4,4.4,6,5.7,7.1c-1.9,1.2-3.8,2.2-5.8,2.9c0-0.1,0-0.1,0-0.2
            c-0.4-2.1-3.2-2.6-4.2-2.6c-1,0-3.8,0.4-4.2,2.6c0,0.1,0,0.1,0,0.2c-2-0.7-4-1.7-5.8-2.9C158,128.4,161,125.8,162.4,122.4z
             M171.9,121.9c3.8-2.7,8.7-7.9,13.7-16.5c2.8,2.5,6.7,3.5,8.4,3.8c-4.3,8.4-9.9,15.3-16.4,19.7C176.5,128.1,173.2,125.4,171.9,121.9
            z M186,104.6c2.7-4.8,5-9.9,6.7-15.2c1.2-3.9,2.1-7.9,2.7-11.9c0.6-4.3,0.8-8.6,0.6-12.9c0-1.2-0.2-3.1-0.5-5.4
            c4.3-0.4,7.6-1.7,9.3-2.6c0.9,17.4-3,36.8-10.5,51.7C193,108.2,188.8,107.2,186,104.6z M195.4,58.5c-1-6.2-4.1-14.3-10.7-18.5
            c7.8-4.6,11.2-9.7,12-11.7c4.4,6.2,7.4,15.5,8.1,27.4C203.3,56.6,199.9,58.1,195.4,58.5z M161.9,138.3c-3.4-1.1-6.6-2.7-9.5-4.8
            c-1.9-1.3-3.6-2.7-5.3-4.3c-1.3-1.3-2.6-2.6-3.8-4c-0.8-1-1.6-1.9-2.4-2.9c-3.6-4.8-6.7-10.2-9.1-15.7c-2.3-5.4-4.2-10.9-5.6-16.6
            c-1.4-5.8-2.4-11.7-2.9-17.6c-0.5-5.8-0.6-11.8-0.1-17.6c0.3-3.1,0.6-6.3,1.2-9.4c0.6-3.1,1.3-6.1,2.3-9.1c0.9-2.7,2-5.4,3.4-7.9
            c1.4-2.6,3.1-5.1,5.1-7.2c1.9-2,4-3.7,6.4-5c1-0.5,2.1-1,3.1-1.4c0.5-0.2,1.1-0.4,1.7-0.5c0.5-0.1,1.1-0.4,1.7-0.4
            c1.4-0.3,2.8-0.4,4.2-0.4c1.1,0,3.9,0.2,6.3,1.7c2.7,1.9,5,5,5.4,9.8c0.6,7.3-6.8,8.5-8.3,8.5c-0.2,0-0.3-0.2,0.4-1
            c1.7-2.2,2.3-5.6,1.7-8.3c-0.7-3.4-3.1-6-6.5-6c-5.2,0-9.8,2.6-13.2,6.3c-3.3,3.6-5.6,8.2-7.3,12.8c-2.1,5.9-3.2,12.2-3.6,18.5
            c-0.4,5.8-0.2,11.6,0.4,17.3c0.6,5.8,1.7,11.6,3.2,17.2c1.5,5.5,3.4,10.9,5.7,16c2.4,5.2,5.3,10.2,8.9,14.7
            c3.6,4.5,8.1,8.5,13.2,11.2c1.2,0.6,2.4,1.1,3.6,1.6c-0.1,1.9,0.4,3.6,1.1,5C162.6,138.6,162.6,138.5,161.9,138.3z M167.3,140.8
            c0,0-0.3,0.3-0.5,0.3c-0.3,0-0.5-0.3-0.5-0.3c-1.3-1.6-3.2-4.7-2.5-8.4c0.2-1,2-1.5,3-1.5c1,0,2.8,0.5,3,1.5
            C170.4,136.1,168.5,139.2,167.3,140.8z M210.2,72.4c-0.5,5.9-1.5,11.8-2.9,17.6c-1.4,5.7-3.2,11.2-5.6,16.6
            c-2.4,5.5-5.4,10.9-9.1,15.7c-0.8,1-1.5,2-2.4,2.9c-1.2,1.4-2.5,2.7-3.8,4c-1.6,1.5-3.4,3-5.3,4.3c-2.9,2-6.2,3.7-9.5,4.8
            c-0.7,0.2-0.7,0.3-1.5,0.4c0.7-1.4,1.2-3.1,1.1-5c1.2-0.4,2.4-1,3.6-1.6c5.2-2.6,9.6-6.7,13.2-11.2c3.6-4.5,6.5-9.5,8.9-14.7
            c2.4-5.2,4.3-10.5,5.7-16c1.5-5.6,2.6-11.4,3.2-17.2c0.6-5.7,0.8-11.5,0.4-17.3c-0.4-6.3-1.5-12.6-3.6-18.5
            c-1.6-4.6-3.9-9.2-7.3-12.8c-3.4-3.7-8-6.3-13.2-6.3c-3.4,0-5.8,2.6-6.5,6c-0.6,2.7,0,6.1,1.7,8.3c0.7,0.8,0.6,1,0.4,1
            c-1.5,0-9-1.1-8.3-8.5c0.4-4.8,2.8-7.9,5.4-9.8c2.4-1.5,5.2-1.7,6.3-1.7c1.4,0,2.8,0.1,4.2,0.4c0.5,0,1.2,0.2,1.7,0.4
            c0.6,0.2,1.1,0.3,1.7,0.5c1.1,0.4,2.1,0.9,3.1,1.4c2.4,1.3,4.6,3.1,6.4,5c2,2.1,3.7,4.6,5.1,7.2c1.4,2.5,2.5,5.2,3.4,7.9
            c1,3,1.8,6,2.3,9.1c0.6,3.1,0.9,6.2,1.2,9.4C210.8,60.6,210.7,66.5,210.2,72.4z"/>
                                <path d="M180.3,54.4v3l0.6-0.1c0,0,0.1,0,0.3,0c0.3,0,0.9,0.1,1.3,0.5c0.4,0.4,0.5,1,0.3,1.8c0,0-6.1,26.3-7.2,31.3
            C174.3,84.6,167,50,167,50l0-0.1h-1.4l0,0.2c0,0-5.2,33.7-6.3,40.4c-1.4-5.8-7.2-30.8-7.2-30.8c0-0.2,0-0.3,0-0.5
            c0-0.7,0.2-1.2,0.5-1.5c0.4-0.4,1.1-0.4,1.2-0.4l0.6,0.1l0-2.9h-10.1v2.9l0.4,0.1c0.2,0,1.4,0.3,1.5,1.4c0,0,11.9,51,12.4,53.1
            l0,0.1h1.8l0-0.2c0.1-0.4,4.9-32,5.8-37.9c1.1,5.9,7.3,37.5,7.4,37.9l0,0.1h1.6L187,58.7c0.3-1.1,1.2-1.2,1.6-1.2c0.1,0,0.1,0,0.1,0
            l0.6,0v-3.1H180.3z"/>
                            </svg>
                        </a>
                    </div>
                    <div id="footer-container-2">
                        <div>
                            <ul>
                                <li>
                                    <a href="/partners">Partners</a>
                                </li>
                                <li>
                                    <a href="/employment">Jobs</a>
                                </li>
                                <li>
                                    <a href="/disclaimer">Disclaimer</a>
                                </li>
                            </ul>
                            <ul>
                                <li>
                                    <a href="https://groups.ssec.wisc.edu/employee-info">For Employees</a>
                                </li>
                                <li>
                                    <a href="/staff/">Staff Directory</a>
                                </li>
                                <li>
                                    <a href="https://research.wisc.edu/">OVCRGE</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div id="footer-container-3">
                        <div id="contact-links">
                            <a href="/contact-us">
                                <svg id="contact-us-icon" data-name="Facebook Icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" aria-hidden="true" role="presentation">
                                    <title>Contact Us</title>
                                    <path id="envelope-path-1" data-name="Envelope Path 1" class="contact-links-path" d="M521.958,257.42H70v565.9L125.154,877l778.288-1.534L954,821.79V257.42H521.958Z" transform="scale(1.4 1.4) translate(-170 -170)"/>
                                    <path id="envelope-path-2" data-name="Envelope Path 2" class="contact-links-path" d="M521.958,628.555L70,327.966V200.676L125.154,147l778.288,1.534L954,202.21V327.966Z" transform="scale(1.4 1.4) translate(-170 -170)"/>
                                </svg>
                                <span class="vertically-align-links">Contact Us</span>
                            </a>
                            <a href="/give" style="margin-left: -2px;">
                                <svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 387.13 451.72" aria-hidden="true" role="presentation">
                                    <title>Give</title>
                                    <path class="contact-links-path" d="M224.11,452.35c-165.61.45-44.07,0-65.71,0-4.5,0-9.06-.51-13.48.08-13.17,1.74-23.79-2.72-34.43-10.5-23.24-17-47.29-32.92-71.12-49.1-3.28-2.23-4.46-8.56-4.46-8.56s.13-67.32,0-101a19.26,19.26,0,0,0-1.85-8.58C16.45,243,5.42,209.46,2,173.74-2.21,129.44,4.4,87.3,29.67,49.59,41.46,32,55.53,16.31,75.6,7.8c21.46-9.11,43.62-7.91,65.72-2.61,34.81,8.35,65,25.95,93.33,47.21,2,1.5,4,3,5.9,4.62,4.48,3.94,5.14,9.18,1.77,13.24s-8.36,4.28-13.2.44c-21.78-17.26-45.06-32-71.17-41.86-15.66-5.92-31.74-9.81-48.67-9.62-31.84.36-53.93,18.14-60.93,49.23-5.56,24.71-2.59,49.06,3.55,73.13,11.87,46.51,34.23,87.92,62.88,126.05,29.09,38.7,63.32,72,104.83,97.38,22.46,13.72,46.13,24.69,72.54,28.07,21.94,2.81,42.74.21,59.16-16.71C363.11,364.21,368,348.9,369.6,332.5c3.6-36-6.21-69.57-19.45-102.42-2.24-5.55-5-10.9-7.28-16.42-2.14-5.08-.25-10,4.41-12.18s9.38-.52,11.71,4.42c17.32,36.61,29.79,74.61,28.93,115.62-.26,12.47-2.69,25.2-6.1,37.24-7.29,25.74-26.49,41.18-49.33,52.75-30.23,15.32-62.29,19.41-95.65,15.22-36.37-4.57-69.35-18.07-100-37.75-6.21-4-11.45-3.47-14.86,1.48s-1.89,11,4.13,14.85C152.32,422.26,180.37,435,211,441.66,217.24,443,389.71,451.9,224.11,452.35Z" transform="translate(-0.83 -1.07)"/>
                                    <path class="contact-links-path" d="M294,131.59c-7.35.78-14.72,1.49-22.06,2.34q-50.1,5.79-100.2,11.62c-1.49.17-3,.25-4.49.31-5.83.21-9.69-2.76-10.28-7.88-.66-5.7,2.63-10.05,8.5-10.89,5.6-.8,11.24-1.35,16.86-2q54.33-6.17,108.67-12.27c3.15-.35,33.83,41.59,32,47.18-12.64,38-25.49,76-38.24,114-1.31,3.92-2.83,7.56-7.36,8.64-8,1.89-13.6-4.86-10.9-13.29,3.24-10.13,6.79-20.17,10.19-30.26q14.58-43.26,29.15-86.52c.47-1.39.81-2.82,1.61-5.65-2.22,1.59-3.47,2.4-4.63,3.34q-57,46.39-113.93,92.81a53.18,53.18,0,0,1-5.12,4,8.87,8.87,0,0,1-11.78-1.6,9.52,9.52,0,0,1-.36-12.38,25.54,25.54,0,0,1,3.7-3.33l113.92-92.82c1.75-1.43,3.46-2.9,5.19-4.36Z" transform="translate(-0.83 -1.07)"/>
                                    <rect class="contact-links-path" x="306.08" y="79.07" width="55.91" height="77.24" rx="7.97" ry="7.97" transform="translate(246 -226.65) rotate(56.34)"/>
                                </svg>
                                <span class="vertically-align-links">Give</span>
                            </a>
                        </div>
                        <div id="social-media-icons">
                            <a href="http://www.facebook.com/uwssec" aria-label="Facebook">
                                <svg id="facebook-icon" data-name="Facebook Icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" aria-hidden="true" role="presentation">
                                    <title>Facebook</title>
                                    <circle class="svg-circle" cx="512" cy="512" r="512"/>
                                    <path class="svg-path" d="M548 6.857v150.857h-89.714q-49.143 0-66.286 20.571t-17.143 61.714v108h167.429l-22.286 169.143h-145.143v433.714h-174.857v-433.714h-145.714v-169.143h145.714v-124.571q0-106.286 59.429-164.857t158.286-58.571q84 0 130.286 6.857z" transform="translate(230 80) scale(0.9 0.9)"/>
                                </svg>
                            </a>
                            <a href="https://twitter.com/uwssec" aria-label="Twitter">
                                <svg id="twitter-icon" data-name="Twitter Icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" aria-hidden="true" role="presentation">
                                    <title>Twitter</title>
                                    <circle class="svg-circle" cx="512" cy="512" r="512"/>
                                    <path class="svg-path" d="M925.714 233.143q-38.286 56-92.571 95.429 0.571 8 0.571 24 0 74.286-21.714 148.286t-66 142-105.429 120.286-147.429 83.429-184.571 31.143q-154.857 0-283.429-82.857 20 2.286 44.571 2.286 128.571 0 229.143-78.857-60-1.143-107.429-36.857t-65.143-91.143q18.857 2.857 34.857 2.857 24.571 0 48.571-6.286-64-13.143-106-63.714t-42-117.429v-2.286q38.857 21.714 83.429 23.429-37.714-25.143-60-65.714t-22.286-88q0-50.286 25.143-93.143 69.143 85.143 168.286 136.286t212.286 56.857q-4.571-21.714-4.571-42.286 0-76.571 54-130.571t130.571-54q80 0 134.857 58.286 62.286-12 117.143-44.571-21.143 65.714-81.143 101.714 53.143-5.714 106.286-28.571z" transform="translate(170 90) scale(0.8 0.8)"/>
                                </svg>
                            </a>
                            <a href="http://www.youtube.com/uwssec" aria-label="Youtube">
                                <svg id="youtube-icon" data-name="Youtube Icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" aria-hidden="true" role="presentation">
                                    <title>Youtube</title>
                                    <circle class="svg-circle" cx="512" cy="512" r="512"/>
                                    <path class="svg-path" d="M554.857 710.857v120.571q0 38.286-22.286 38.286-13.143 0-25.714-12.571v-172q12.571-12.571 25.714-12.571 22.286 0 22.286 38.286zM748 711.429v26.286h-51.429v-26.286q0-38.857 25.714-38.857t25.714 38.857zM196 586.857h61.143v-53.714h-178.286v53.714h60v325.143h57.143v-325.143zM360.571 912h50.857v-282.286h-50.857v216q-17.143 24-32.571 24-10.286 0-12-12-0.571-1.714-0.571-20v-208h-50.857v223.429q0 28 4.571 41.714 6.857 21.143 33.143 21.143 27.429 0 58.286-34.857v30.857zM605.714 827.429v-112.571q0-41.714-5.143-56.571-9.714-32-40.571-32-28.571 0-53.143 30.857v-124h-50.857v378.857h50.857v-27.429q25.714 31.429 53.143 31.429 30.857 0 40.571-31.429 5.143-15.429 5.143-57.143zM798.857 821.714v-7.429h-52q0 29.143-1.143 34.857-4 20.571-22.857 20.571-26.286 0-26.286-39.429v-49.714h102.286v-58.857q0-45.143-15.429-66.286-22.286-29.143-60.571-29.143-38.857 0-61.143 29.143-16 21.143-16 66.286v98.857q0 45.143 16.571 66.286 22.286 29.143 61.714 29.143 41.143 0 61.714-30.286 10.286-15.429 12-30.857 1.143-5.143 1.143-33.143zM451.429 300v-120q0-39.429-24.571-39.429t-24.571 39.429v120q0 40 24.571 40t24.571-40zM862.286 729.143q0 133.714-14.857 200-8 33.714-33.143 56.571t-58.286 26.286q-105.143 12-317.143 12t-317.143-12q-33.143-3.429-58.571-26.286t-32.857-56.571q-14.857-64-14.857-200 0-133.714 14.857-200 8-33.714 33.143-56.571t58.857-26.857q104.571-11.429 316.571-11.429t317.143 11.429q33.143 4 58.571 26.857t32.857 56.571q14.857 64 14.857 200zM292 0h58.286l-69.143 228v154.857h-57.143v-154.857q-8-42.286-34.857-121.143-21.143-58.857-37.143-106.857h60.571l40.571 150.286zM503.429 190.286v100q0 46.286-16 67.429-21.143 29.143-60.571 29.143-38.286 0-60-29.143-16-21.714-16-67.429v-100q0-45.714 16-66.857 21.714-29.143 60-29.143 39.429 0 60.571 29.143 16 21.143 16 66.857zM694.857 97.714v285.143h-52v-31.429q-30.286 35.429-58.857 35.429-26.286 0-33.714-21.143-4.571-13.714-4.571-42.857v-225.143h52v209.714q0 18.857 0.571 20 1.714 12.571 12 12.571 15.429 0 32.571-24.571v-217.714h52z" transform="translate(200 130) scale(0.7 0.7)"/>
                                </svg>
                            </a>
                            <a href="https://www.instagram.com/uwaossbldg/" aria-label="Instagram">
                                <svg id="instagram-icon" data-name="Instagram Icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" aria-hidden="true" role="presentation">
                                    <title>Instagram Icon</title>
                                    <circle class="svg-circle" cx="512" cy="512" r="512"/>
                                    <path class="svg-path" transform="translate(185 100) scale(0.75, 0.75)" d="M778.286 814.857v-370.286h-77.143q11.429 36 11.429 74.857 0 72-36.571 132.857t-99.429 96.286-137.143 35.429q-112.571 0-192.571-77.429t-80-187.143q0-38.857 11.429-74.857h-80.571v370.286q0 14.857 10 24.857t24.857 10h610.857q14.286 0 24.571-10t10.286-24.857zM616 510.286q0-70.857-51.714-120.857t-124.857-50q-72.571 0-124.286 50t-51.714 120.857 51.714 120.857 124.286 50q73.143 0 124.857-50t51.714-120.857zM778.286 304.571v-94.286q0-16-11.429-27.714t-28-11.714h-99.429q-16.571 0-28 11.714t-11.429 27.714v94.286q0 16.571 11.429 28t28 11.429h99.429q16.571 0 28-11.429t11.429-28zM877.714 185.714v652.571q0 46.286-33.143 79.429t-79.429 33.143h-652.571q-46.286 0-79.429-33.143t-33.143-79.429v-652.571q0-46.286 33.143-79.429t79.429-33.143h652.571q46.286 0 79.429 33.143t33.143 79.429z"></path>
                                </svg>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="wrapper" id="address-container">
                    <address>
                        &copy;2024
    		The Board of Regents of the University of Wisconsin System
    		<br>
                        1225 W. Dayton St.
    		<span class="separator">&middot;</span>
                        Madison, WI 53706, USA
    		<span class="separator">&middot;</span>
                        608-263-6750
    	
                    </address>
                </div>
            </div>
        </body>
    </html>
""".trimIndent()
