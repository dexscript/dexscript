# assignable_relationship


| assignable | to          | from  |
| ---------- | ----------- | ----- |
| true       | `'a'\|'b'` | `'a'` |
| true       | `'a'\|'b'` | `'b'` |
| true       | `'a'\|'b'\|'c'` | `'a'\|'b'` |
| false       | `'a'\|'b'` | `'a'\|'b'\|'c'` |
