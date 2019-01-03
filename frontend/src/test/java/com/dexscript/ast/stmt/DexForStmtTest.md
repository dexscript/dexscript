# forever

```dexscript
for {}
```

# for_condition

```dexscript
for a==1 {}
```

* condition
    * "a==1"

# for_with_init_and_post

```dexscript
for i := 0; i < 10; i++ {}
```

* initStmt
    * `i := 0`
* condition
    * `i < 10`
* postStmt
    * `i++`

# init_is_optional

```dexscript
for ; i < 10; i++ {}
```

* initStmt
    * null
* condition
    * `i < 10`
* postStmt
    * `i++`

# post_is_optional

```dexscript
for i := 0 ; i < 10; {}
```

* initStmt
    * `i := 0`
* condition
    * `i < 10`
* postStmt
    * null

# init_and_post_is_optional

```dexscript
for ; i < 10; {}
```

* initStmt
    * null
* condition
    * `i < 10`
* postStmt
    * null

# for_with_3_semi_colon

```dexscript
for ;; {}
```


