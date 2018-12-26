# matched

* hello
* " hello "
* "\thello\t"
* "\rhello\r"
* "\nhello\n"

hello() will match the hello part

* " hello()"

# unmatched

* 0
* 12
* $

# package_qualifier

```dexscript
a.b
```

* "a.b"
* pkgName
    * "a"
* typeName
    * "b"

# no_package_qualifier

```dexscript
a
```

* "a"
* pkgName
    * ""
* typeName
    * "a"

