# HariboteCodeGenerator
## 概要
HariboteCodeGeneratorは、HariboteOSで動くバイナリファイルを生成できるプログラムです。
## 使用方法
まず、プログラムを、
```
HariboteCodeGenerator/wplace
```
に置きます(C言語)

そして
```
java -jar Make.jar ProgramName.c
```
でコンパイルされます。

オブジェクトファイルは
```
HariboteCodeGenerator/bin/app/tmp/ProgramName/
```
にあります。

実行ファイル(.hrb)は、
```
HariboteCodegenerator/bin/app/dist
```
にProgramName.hrbという感じに保存されています。

## 実行方法
HariboteOS上で実行可能です。