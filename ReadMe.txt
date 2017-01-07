Project 1
B00599170

Compilation
1. Unzip tar ball
2. Use make command to compile source code

Execution
1. Run server.sh with port number as a command line argument.
2. Run client.sh in another terminal or machine with 8 arguments, machinename port --operation read/write/list --filename input_file.txt --user xyz. Where last 3 arguments can be in any combination.

Input-Output:
$> ./client remote01.cs.binghamton.edu 9090 --operation write --filename ex_file1 --user guest
{"1":{"i32":1}}
$> ./client remote01.cs.binghamton.edu 9090 --operation write --filename ex_file2 --user guest
{"1":{"i32":1}}
$> ./client remote01.cs.binghamton.edu 9090 --operation list --user guest
["rec",2,{"1":{"str":"ex_file1"},"2":{"i64":1441391699104},"3":{"i64":1441391699104},
"4":{"i32":0},"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"dd75ce3f5c3f89878459545c697f87de"}},
{"1":{"str":"ex_file2"},"2":{"i64":1441391722853},"3":{"i64":1441391722853},"4":{"i32":0},
"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"1f600d1084e1455bb7b309a50dd97085"}}]
$> ./client remote01.cs.binghamton.edu 9090 --operation list --user nonexist
{"1":{"str":"User nonexist does not exist."}}
$> ./client remote01.cs.binghamton.edu 9090 --operation read --filename ex_file1 --user guest
{"1":{"rec":{"1":{"str":"ex_file1"},"2":{"i64":1441391699104},"3":{"i64":1441391699104},
"4":{"i32":0},"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"dd75ce3f5c3f89878459545c697f87de"}}},
"2":{"str":"example content1\n"}}
$> ./client remote01.cs.binghamton.edu 9090 --operation read --filename ex_file2 --user guest
{"1":{"rec":{"1":{"str":"ex_file2"},"2":{"i64":1441391722853},"3":{"i64":1441391722853},
"4":{"i32":0},"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"1f600d1084e1455bb7b309a50dd97085"}}},
"2":{"str":"example content2\n"}}
$> vi ex_file1
$> ./client remote01.cs.binghamton.edu 9090 --operation write --filename ex_file1 --user guest
{"1":{"i32":1}}
$> ./client remote01.cs.binghamton.edu 9090 --operation read --filename ex_file1 --user guest
{"1":{"rec":{"1":{"str":"ex_file1"},"2":{"i64":1441391699104},"3":{"i64":1441391833690},
"4":{"i32":1},"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"c2907ca1ae7a101fb58a093a081d7b4c"}}},
"2":{"str":"modified content\n"}}
$> ./client remote01.cs.binghamton.edu 9090 --operation list --user guest
["rec",2,{"1":{"str":"ex_file1"},"2":{"i64":1441391699104},"3":{"i64":1441391833690},
"4":{"i32":1},"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"c2907ca1ae7a101fb58a093a081d7b4c"}},
{"1":{"str":"ex_file2"},"2":{"i64":1441391722853},"3":{"i64":1441391722853},"4":{"i32":0},
"5":{"str":"guest"},"6":{"i32":17},"7":{"str":"1f600d1084e1455bb7b309a50dd97085"}}]