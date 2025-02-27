import 'package:appbank/screens/login_screen.dart';
import 'package:flutter/material.dart';

class Start extends StatelessWidget {
  const Start({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey,
      appBar: PreferredSize(
        preferredSize: Size.fromHeight(80),
        child: ClipRRect(
          borderRadius: BorderRadius.vertical(
            bottom: Radius.circular(25),
          ),
          child: AppBar(
            title: Text("imagem logo"),
            backgroundColor: Colors.white,
            elevation: 0,
          ),
        ) 
      ),
      body: Column(
        children: [
          Padding(
            padding: EdgeInsets.only(top: 600),
            child: Center(
              child: ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context, 
                    MaterialPageRoute(builder: (context) => Login())
                  );
                }, 
                child: Text('Log in')
              ),
            ),
          )
        ],
      ),
    );
  }
}