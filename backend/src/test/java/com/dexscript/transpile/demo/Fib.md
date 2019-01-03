# print_fib

```dexscript
function Hello() {
    a := 1
    b := 1
    for {
        ::STDIN.read()
        c := a + b
        print(c, stream=::STDERR)
        a = b
        b = c
    }
}
```
