def every_other_char(s):
    return s[::2]

# Example usage
if __name__ == "__main__":
    examples = ["", "a", "house", "abcd", "hello", "Python is awesome!"]

    for example in examples:
        result = every_other_char(example)
        print(f"Original: '{example}'")
        print(f"Every other char: '{result}'")
        print()