# missing_left_paren

```dexscript
function hello ) {
}
```

```dexscript
<unmatched>function hello ) {
}</unmatched>
```

# skip_garbage_in_prelude

```dexscript
 example function hello () {
}
```

```dexscript
<error/>function hello () {
}
```

