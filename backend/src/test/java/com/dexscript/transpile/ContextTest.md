# use_context

```dexscript
// /pkg1/__spi__.ds

interface :: {
    F1()
}
```

```dexscript
// /pkg1/hello.ds
function Hello() {
   ctx := new Context()
   return F1($=ctx)
}

function Context() {
   await {
   case GetPid(): string {
       resolve 'hello' -> GetPid
   }}
}
```

```dexscript
// /pkg2/__spi__.ds

interface :: {
}
interface $ {
    GetPid(): string
}
```

```dexscript
// /pkg2/123.ds

function F1(): string {
    return F2()
}
function F2(): string {
    return $.GetPid()
}
```

* "hello"



