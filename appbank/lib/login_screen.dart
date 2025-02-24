import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class BackgroundPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final Paint paint = Paint();

    paint.color = Colors.pink.shade100.withOpacity(0.9);
    canvas.drawCircle(Offset(size.width * 0.1, size.height * 0.9), 180, paint);

    paint.color = Colors.pink.shade100.withOpacity(0.5);
    canvas.drawCircle(Offset(size.width * 0.4, size.height * 0.8), 120, paint);

    paint.color = Colors.pink.shade200.withOpacity(0.8);
    canvas.drawCircle(Offset(size.width * 0.7, size.height * 0.9), 200, paint);

    paint.color = Colors.pink.shade200.withOpacity(0.4);
    canvas.drawCircle(Offset(size.width * 0.9, size.height * 0.82), 200, paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return false;
  }
}

class Login extends StatelessWidget {
  Login({super.key});

  final TextEditingController _controllerEmail = TextEditingController();
  final TextEditingController _controllerPassword = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: CustomPaint(
        painter: BackgroundPainter(),
        child: Center(
          child: Column(
            children: [
              Image(
                height: 125,
                image: AssetImage('ivibank_logo.png')
              ),
              Padding(
                padding: const EdgeInsets.all(50.0),
                child: Text(
                  "Login",
                  style: GoogleFonts.montserrat(
                    fontSize: 40, 
                    fontWeight: FontWeight.bold
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 50),
                child: SizedBox(
                  width: 350,
                  child: TextField(
                    controller: _controllerEmail,
                    keyboardType: TextInputType.emailAddress,
                    decoration: InputDecoration(
                      hintText: "Email",
                      prefixIcon: Icon(Icons.email, color: Colors.black),
                      focusedBorder: UnderlineInputBorder(
                        borderSide: BorderSide(color: Colors.pinkAccent)
                      )
                    ),
                  )
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 100),
                child: SizedBox(
                  width: 350,
                  child: TextField(
                    controller: _controllerPassword,
                    keyboardType: TextInputType.text,
                    decoration: InputDecoration(
                      hintText: "Password",
                      prefixIcon: Icon(Icons.password, color: Colors.black),
                      focusedBorder: UnderlineInputBorder(
                          borderSide: BorderSide(color: Colors.pinkAccent)
                      )
                    ),
                  )
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 125),
                child: ElevatedButton(
                  onPressed: () {
                    print("button login test");
                  }, 
                  child: Text (
                    "login",
                    style: GoogleFonts.montserrat(
                      fontSize: 15, 
                      fontWeight: FontWeight.w500
                    ),
                  ),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Color.fromARGB(255, 255, 115, 162),
                    foregroundColor: Colors.black,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30)
                    )
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(25.0),
                child: GestureDetector(
                  onTap: () {
                    print("create account button");
                  },
                  child: Text(
                    "create account",
                    style: GoogleFonts.montserrat(
                        fontSize: 15, 
                        fontWeight: FontWeight.w500
                    ),
                  ),
                ),
              ),
              GestureDetector(
                onTap: () {
                  print("forgot password button");
                },
                child: Text(
                  "forgot password",
                  style: GoogleFonts.montserrat(
                    fontSize: 15, 
                    fontWeight: FontWeight.w500
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
