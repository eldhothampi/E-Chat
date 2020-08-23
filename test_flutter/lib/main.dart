import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

int count = 0;
void main() {
  runApp(
    MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('E Cart'),
          backgroundColor: (Colors.blueGrey[900]),
        ),
        body: Center(
          child: (Image(
            image: AssetImage('images/dani.jpg')
          )

          ),
        ),

        floatingActionButton: FloatingActionButton(
          child: Icon(Icons.add),
        ),
      ),
    ),
  );
}
