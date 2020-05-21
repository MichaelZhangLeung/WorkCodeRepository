import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';
import 'package:vpn/quantum_bridge.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => new HomePageState();
}

class HomePageState extends State<HomePage> {
  String _htmlPath = 'android_asset/dashboard/index.html';
  InAppWebViewController _controller;
  QuantumBridge _bridge = new QuantumBridge();
  static const quantumChannel = const MethodChannel("quantum_vpn/channel_1");

  @override
  void initState() {
    super.initState();
    _bridge.setChannel(quantumChannel);
  }

  void _test() async {}

  @override
  Widget build(BuildContext context) {
//    _test();
    return new Scaffold(
      body: _buildInapp(),
    );
  }

  Widget _buildInapp() {
    return new Container(
        child: Column(children: <Widget>[
      Expanded(
        child: Container(
          child: InAppWebView(
            initialUrl: "http://localhost:8080/$_htmlPath",
            initialOptions: InAppWebViewWidgetOptions(
                inAppWebViewOptions: InAppWebViewOptions(
                  javaScriptEnabled: true,
                  debuggingEnabled: true,
            )),
            onWebViewCreated: (InAppWebViewController controller) {
                _controller = controller ;
                registerJavaScriptHandler() ;
            },
            onConsoleMessage: (InAppWebViewController controller, ConsoleMessage consoleMessage) {
              print("console message: ${consoleMessage.message}");
            },
            onLoadStart: (InAppWebViewController controller, String url) {},
            onLoadStop: (InAppWebViewController controller, String url) {
//              print("load stop");
//              _controller.evaluateJavascript(source: """
//    window.flutter_inappwebview.callHandler('test', 'Text from Javascript').then(function(result) {
//      console.log(result);
//    });
//  """);
            },
          ),
        ),
      )
    ]));
  }

  void registerJavaScriptHandler(){
    print("registerJavaScriptHandler in");
    QuantumBridge.jsCallbacks.forEach((value) {
      _controller.addJavaScriptHandler(handlerName: value.name, callback: value.callback) ;
    }) ;
    _controller.addJavaScriptHandler(handlerName: 'test', callback: (args){
      print("js called$args");
      showMySimpleDialog(context);
      return '{}';
    }) ;
  }

  void showMySimpleDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) {
          return new SimpleDialog(
            title: new Text("testing dialog"),
            children: <Widget>[
              new SimpleDialogOption(
                child: new Text("TESTING"),
                onPressed: () {
                  Navigator.of(context).pop("SimpleDialogOption One");
                },
              )
            ],
          );
        });
  }

//  Widget _buildWebView() {
//    return new Container(
//      child: Center(
//        child: WebView(
//          initialUrl: '',
//          //'file:///flutter_assets/android_asset/dashboard/index.html',//'file:///android_asset/dashboard/index.html',
//          userAgent: "ClashX Runtime",
//          javascriptMode: JavascriptMode.unrestricted,
//          debuggingEnabled: true,
//          onWebViewCreated: (WebViewController controller) {
//            _controller = controller;
//            _loadLocalHtml();
//          },
//        ),
//      ),
//    );
//  }
//
//  _loadLocalHtml() async {
//    String htmlContents = await rootBundle.loadString(_htmlPath);
//    _controller.loadUrl(Uri.dataFromString(htmlContents,
//            mimeType: 'text/html', encoding: Encoding.getByName('utf-8'))
//        .toString());
//  }
}
