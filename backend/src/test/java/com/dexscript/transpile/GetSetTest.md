# index_get_by_int32

```dexscript
function Hello(): string {
   obj := new World()
   return obj[0]
}
function World() {
   await {
   case get(index: int32): string {
       resolve 'hello' -> get
   }}
}
```

* "hello"

# index_get_by_string

```dexscript
function Hello(): string {
   obj := new World()
   return obj['field']
}
function World() {
   await {
   case get(index: 'field'): string {
       resolve 'hello' -> get
   }}
}
```

* "hello"

# field_get

```dexscript
function Hello(): string {
   obj := new World()
   return obj.field
}
function World() {
   await {
   case getField(): string {
       resolve 'hello' -> getField
   }}
}
```

* "hello"