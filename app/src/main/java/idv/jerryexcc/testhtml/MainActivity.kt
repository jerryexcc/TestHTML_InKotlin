package idv.jerryexcc.testhtml

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import com.e.testhtml.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private val url_string: String? = "file:///android_asset/testJS.html"
    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if(edText.text.toString().trim().isEmpty()){//判斷user是否有填資料,並且檢查是否為空白字元
                toast("請打字")
                return@setOnClickListener
            }
            //呼叫mobileCallJs這支JS並把editText的文字擷取轉成字串
            webView!!.loadUrl("javascript:mobileCallJs('${edText.text}')")
        }

        webView = findViewById(R.id.webView)
        webView!!.settings.javaScriptEnabled = true//允許使用 javascript
        webView!!.settings.domStorageEnabled = true//HTML 可在Local端儲存資料

        webView!!.addJavascriptInterface(JavaScriptInterface(), "android_app")//JS與Android溝通用的Interfacem

        webView!!.webChromeClient = WebChromeClient()//webview 才會顯示 alert
        webView!!.webViewClient = object : WebViewClient(){
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request!!.url.toString())
                return true
            }
        }
        webView!!.loadUrl(url_string)//讀取網頁
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun callAndroid(param: String) {
            toast(param)
        }
    }
}
