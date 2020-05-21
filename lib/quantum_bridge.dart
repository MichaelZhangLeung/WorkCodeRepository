import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

class QuantumBridge{

  MethodChannel _channel ;
  InAppWebViewController _controller ;
  static List<JsCallback> jsCallbacks = new List(4);

  QuantumBridge(){
    jsCallbacks.add(JsCallback(name:'checkEnable', callback: (args) async {
      return await checkEnable() ;
    }));
    jsCallbacks.add(JsCallback(name:'startVpn', callback: (args){
      return startVpn() ;
    }));
    jsCallbacks.add(JsCallback(name:'stopVpn', callback: (args){
      return stopVpn() ;
    }));
    jsCallbacks.add(JsCallback(name:'getRESTfulApi', callback: (args) async {
      return await getRESTfulApi() ;
    }));
    register() ;
  }

  void register(){
    MethodChannel channel = MethodChannel("quantum_vpn/channel_2");
    channel.setMethodCallHandler(_methodCallHandler) ;
  }

  void setChannel(MethodChannel channel){
    _channel = channel ;
  }

  void setControl(InAppWebViewController controller){
    _controller = controller ;
  }

  Future<dynamic> _methodCallHandler(MethodCall methodCall) async{
    switch(methodCall.method){
      case "loadJs":
        Map map = methodCall.arguments ;
        if(_controller != null){
          _controller.evaluateJavascript(source: map['js']) ;
        }
    }
  }

  Future<bool> checkEnable() async {
    bool enable ;
    if(_channel != null){
      enable = await _channel.invokeMethod<bool>("checkEnable") ;
    }
    return enable ;
  }

  void startVpn(){
    if(_channel != null){
      _channel.invokeMethod<bool>("startVpn") ;
    }
  }

  void stopVpn(){
    if(_channel != null){
      _channel.invokeMethod<bool>("stopVpn") ;
    }
  }

  Future<String> getRESTfulApi() async {
    String restFulApi = "" ;
    if(_channel != null){
      restFulApi = await _channel.invokeMethod<String>("getRESTfulApi") ;
    }
    return restFulApi ;
  }
}

class JsCallback{
  String name ;
  JavaScriptHandlerCallback callback ;
  JsCallback({@required String name,
    @required JavaScriptHandlerCallback callback}){
    this.name = name ;
    this.callback = callback ;
  }
}