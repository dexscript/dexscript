# index_invoke_get

```dexscript
function Hello(): string {
   obj := new World()
   return obj[0]
}
function World() {
   await {
   case Get__(index: int32): string {
       resolve 'hello' -> Get__
   }}
}
```

* "hello"