# Remove Invalid Parentheses --- Approach 2: Maximum Length

## Idea

This approach does **not calculate the number of removals first**.

Instead, it generates possible strings by making two choices for every
parenthesis:

1.  **Remove** the character.
2.  **Keep** the character.

Whenever we reach the end of the string, we check whether the
constructed string is balanced.

Among all balanced strings, we keep only the strings having the
**maximum length**.

Why?

Because:

``` text
Maximum length
      =
Minimum number of removals
```

For example, if the original string has length `7`:

``` text
valid answer length = 6
```

means:

``` text
7 - 6 = 1 removal
```

A valid answer of length `5` would require two removals, so it is not
optimal.

------------------------------------------------------------------------

## Core Variables

``` text
i       → current index in the input
curr    → string constructed so far
count   → current parenthesis balance
maxLen  → maximum length of a valid string found
result  → valid strings having maxLen
```

------------------------------------------------------------------------

## Backtracking Choices

For a parenthesis:

``` text
                    current parenthesis
                     /              \
                REMOVE              KEEP
                   |                  |
            don't add it        add it to curr
```

In Java:

### Remove

``` java
dfs(s, i + 1, curr, count);
```

### Keep

``` java
curr.append(ch);
dfs(...);
curr.deleteCharAt(curr.length() - 1);
```

Again, `deleteCharAt()` is the undo operation for the KEEP choice.

------------------------------------------------------------------------

## Base Case

When:

``` java
i == s.length()
```

we have constructed one complete candidate.

First check:

``` java
count == 0
```

This means the parentheses are balanced.

Then compare its length with `maxLen`.

### Case 1: Longer

``` java
curr.length() > maxLen
```

This is a better answer.

``` text
clear old results
update maxLen
add current string
```

### Case 2: Same length

``` java
curr.length() == maxLen
```

This is another optimal answer.

``` text
add it to result
```

### Case 3: Shorter

``` java
curr.length() < maxLen
```

Ignore it because it requires more removals.

------------------------------------------------------------------------

## Java Code

``` java
import java.util.*;

class Solution {

    Set<String> result = new HashSet<>();
    int maxLen = 0;

    void dfs(String s, int i,
             StringBuilder curr, int count) {

        // Invalid branch
        if (count < 0) {
            return;
        }

        // End of string
        if (i == s.length()) {

            // Candidate must be balanced
            if (count == 0) {

                // Found a better (longer) answer
                if (curr.length() > maxLen) {

                    result.clear();

                    maxLen = curr.length();

                    result.add(curr.toString());
                }

                // Another answer with the same
                // minimum number of removals
                else if (curr.length() == maxLen) {

                    result.add(curr.toString());
                }
            }

            return;
        }

        char ch = s.charAt(i);

        // -------------------------
        // 1. REMOVE current char
        // -------------------------

        if (ch == '(' || ch == ')') {

            dfs(s, i + 1,
                curr, count);
        }

        // -------------------------
        // 2. KEEP current char
        // -------------------------

        curr.append(ch);

        if (ch == '(') {

            dfs(s, i + 1,
                curr, count + 1);

        } else if (ch == ')') {

            dfs(s, i + 1,
                curr, count - 1);

        } else {

            dfs(s, i + 1,
                curr, count);
        }

        // Undo KEEP choice
        curr.deleteCharAt(curr.length() - 1);
    }

    public List<String> removeInvalidParentheses(String s) {

        result.clear();
        maxLen = 0;

        dfs(s, 0,
            new StringBuilder(), 0);

        return new ArrayList<>(result);
    }
}
```

------------------------------------------------------------------------

## Example

Input:

``` text
()())()
```

Possible balanced strings include:

``` text
()
(())()
()()()
```

Their lengths are:

``` text
()       → 2
(())()   → 6
()()()   → 6
```

The first one is balanced but shorter, so it is ignored.

The maximum length is:

``` text
maxLen = 6
```

Therefore:

``` text
result = {
    "(())()",
    "()()()"
}
```

------------------------------------------------------------------------

## How `maxLen` Works

Suppose the algorithm finds:

``` text
()
```

First:

``` text
maxLen = 0
```

Since:

``` text
2 > 0
```

we do:

``` text
result.clear();
maxLen = 2;
result.add("()");
```

Later it finds:

``` text
(())()
```

Length:

``` text
6
```

Since:

``` text
6 > 2
```

we do:

``` text
result.clear();
maxLen = 6;
result.add("(())()");
```

Later it finds:

``` text
()()()
```

Length:

``` text
6
```

Since:

``` text
6 == 6
```

we add it without clearing:

``` text
result = {
    "(())()",
    "()()()"
}
```

------------------------------------------------------------------------

## Why `deleteCharAt()` Is Still Needed

This approach avoids calculating `removals`, but it does **not**
eliminate backtracking.

Suppose:

``` text
curr = "("
```

We KEEP `)`:

``` java
curr.append(')');
```

Now:

``` text
curr = "()"
```

We recursively explore the complete subtree below this decision.

After recursion returns:

``` text
curr = "()"
```

But we need to restore the previous state:

``` text
curr = "("
```

So:

``` java
curr.deleteCharAt(curr.length() - 1);
```

restores it.

The pattern is:

``` text
KEEP
 ↓
APPEND
 ↓
RECURSE / EXPLORE
 ↓
RETURN
 ↓
DELETE / UNDO
```

------------------------------------------------------------------------

## Difference From Approach 1

  -------------------------------------------------------------------------------
  Feature                 Approach 1                      Approach 2
  ----------------------- ------------------------------- -----------------------
  Calculate removals      Yes                             No
  first                                                   

  Backtracking            Yes                             Yes

  Keep / Remove choices   Yes                             Yes

  Uses `StringBuilder`    Yes                             Yes

  Uses `deleteCharAt()`   Yes                             Yes

  Final condition         `removals == 0 && count == 0`   `count == 0`

  How optimal answers are Exactly minimum removals        Maximum string length
  selected                                                
  -------------------------------------------------------------------------------

------------------------------------------------------------------------

## Important Observation

Both approaches can explore up to:

``` text
O(2^n)
```

choices in the worst case.

Approach 2 is often easier to understand conceptually:

``` text
Generate → Check balanced → Keep longest
```

Approach 1 is more direct because it determines the minimum number of
removals before searching:

``` text
Calculate removals → Generate only solutions using that number
```

# Summary

### Approach 1

``` text
Find minimum removals
        ↓
Backtracking
        ↓
Use exactly those removals
        ↓
Return balanced strings
```

### Approach 2

``` text
Backtracking
        ↓
Generate candidates
        ↓
Keep balanced candidates
        ↓
Keep only maximum-length candidates
```
