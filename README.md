
# Structured Errors Response toolkit for REST

## A few examples in front of all

### Example 1: Create point
Request:
```json
{
    "x": "200",
    "y": "ten"
}
```

Response:
```json
{
    "x": ["must be less than or equal to 100.0"],
    "y": ["Unable to parse `two` as [double]"]
}
```


### Example 2: Register user
Request:
```json
{
    "name": "Luke",
    "surname": "Skywalker",
    "emails": [
        {"address" : "luke_skywalker@jediorder.sw", "primary" : "true"}
        {"address" : "luke_skywalker@newrepublic.sw", "primary" : "true"}
    ],
    "masters": ["Obi-Wan Kenobi", "Joda"]
}
```

Response:
```json
{
    "dateofbirth": ["may not be null"],
    "emails": ["at least 3 emails are required", "must be exactly one primary email"],
    "masters": {
        "1": ["is not a known Jedi Master"]
    }
}
```

See /src-test-java/tutorial/TutorialTest.java to see how it works

## Overview and motto

The topic of validation is well-researched in Java. There is Bean Validation API (JSR 303/349) specification
and a number of libraries that support it. And there are a lot of libraries for implementing REST.
However, there is no standard or established best practice on what should happen once you combine the two.
Most recommendations for the expectation will propose to return a structure like:

    { "code": "...", "userMessage": "...", "internalMessage": "..." }

And maybe wrap it in the array, if you need multiple errors. If you think such approach is good enough, you
probably should stop reading this.

## Requirements
1. **Error response must be human readable**.
Proper error messages make the life of the developers implementing calls to your API much easier.

2. **Multiple errors must be supported**
Users find it frustrating, when they have resubmit to see what else is wrong with their form.

3. **Structured**, e.g. response should pinpoint the location of each error in the request structure.
Probably, the most common case for validation is having user input some data and then show him the errors.
Frontend should have no problem with parsing your response and binding it to original request structure.

4. **Forward compatibility**
If a new mandatory field in the API is added, old caller implementations should be easily diagnosed by a
developer, merely looking at error response.

5. **Maintainability**
Adding a new field with constraints similar to existing should have predictable behaviour. If validation
rule is exactly the same as the existing one, but for a different field, should not require communicating
anything else other than the fact the the field was added.

## Contract
Signifficant effort was made to make the contract is as simple as it could be:
If one or more validation errors occur, error response is returned with the following properties:

1. HTTP response code is 400 (Bad Request).

2. Media-type is "application/json".
NOTE: Factors 1 and 2 are the recommended way to distinguish validation errors from other HTTP 400 errors,
such as specifying incorrect method or content type. If this approach is conflicting with your existing code,
GsonMessageBodyHandler.buildErrorResponse can be overridden to build response to carry markers, required for
your specific environment.

3. Body is JSON object with the structure that is created from the expected request structure by the
following rules:

3.1. If the object provided in request is valid, matching object is not present in response.

3.2. If the object in request is not valid, in the matching location of response an array of strings is
returned. These strings contain validation error messages. Note that:
location of error message is as specific as possible, and nested
In any place, where an object or primitive is present or expected in the request, an array might appear.
Such array indicates an error that has occured validating that specific object.
3.2. Arrays that already are in the original request strusture correspond to maps in response.
Key is index from array (0-based).

## Processing response
When you are looking at any field or subfield of the response object, the following options are possible:

1. The value is empty/null. This means that matching request object did not trigger any validation errors.

2. The value is object. This means that matching request object was not completely validated itself as some
fields have produced a validation errors of their own. You have to go down into this object for the details.

3. The value is array of strings. This means that validation has failed on this specific object, and you can
get all error messages for it from that array.

