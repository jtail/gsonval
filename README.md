
# Structured Errors Response toolkit for REST

## A few examples in front of all

### Example 1: Create point
Request:
```json
{
    "x": "two",
    "y": "ten"
}
```

Response:
```json
{
    "x": ["Unable to parse `two` as [double]"],
    "y": ["Unable to parse `ten` as [double]"]
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
    "dateofbirth": ["Mandatory"],
    "emails": ["Only one can be set as primary", "At least 3 emails are required"],
    "masters" {
        "1": ["Unknown character name 'Joda'. Maybe you meant: 'Yoda'?"]
    }
}
```


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

4. **Backward compatibility**
Adding



Platform neutral output

