// A slug has the form words-with-sometimes-numbers-like-2
// So the regex is one or more repetition of [a-z0-9]
// followed by zero or more (not captured) groups of hyphen plus [a-z0-9] repetition
export const SLUG_REGEX = /^[a-z0-9]+(?:-[a-z0-9]+)*$/;
