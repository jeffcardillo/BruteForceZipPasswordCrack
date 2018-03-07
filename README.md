# BruteForceZipPasswordCrack
Java tool to help crack a password protected zip file.

I came across a 10+ year old zip for which I password protected but couldn't remember the exact password. I thought it'd be fun to write a tool to crack the password for me!

This tool allows you to specify the exact set of characters to use in password attempts, as well as a range for the length of password. The algorithm I came up with will _cleverly_ generate every possible combination of password given those parameters and try to extract your files from the protected zip file with them.

Truth be told, this program is slow. Luckily, I remembered my password when finishing up the code for this. My password was 10 different characters with a length of 10. That is 10^10 (or 10 Billion) possibilities! Given the exact 10 characters and length, I estimate it'd take about 30 days to crack the password. Not knowing the exact characters to include means you'd have to specify many other characters and possibly a length range, greatly increasing the processing time!

That said, this was a fun experiment. And it does work! And I'm happy with the algorithm I came up with to sequentially generate every possible password combination with a given set of parameters. My approach basically just uses a counter and sets each character in the generated password from the counter value, like:

```
// cleverly set the value of each character in password based on the counter
for (int i=0; i&lt;length; i++) {
   password[i] = chars[(int)((count / (Math.pow(chars.length, i))) % chars.length)];
}
```

### Usage
Below is a sample command showing all arguments. Arguments are optional, however you'll need to actually specify a source zip file and a destination to extract to.

`java -jar BruteForceZipPassword.jar -min 3 -max 6 -characters "abcABC123@#$" -s "/Users/username/Desktop/secret.zip" -d "/Users/username/Desktop/Extracted" -verbose`

#### Arguments
```-min - the minimum length of the password to try
-max - the maximum length of password to try
-characters - the possible characters and symbols in the password
-s - the path to the source zip file
-d - the path to where you want to extract the contents
-verbose - have the program output every password it generates
```
In the above command the program will create every possible combination of password using "abcABC123@#" and from 3 to 6 characters in length.

#### Future
I lost a bit of steam since I discovered my password before completing this project. If I pick this project back up, I'd like to experiment with ForkJoin to parallelize the computations and reduce the overall time to solution.

