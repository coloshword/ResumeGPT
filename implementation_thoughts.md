### Implementation Goals

01/18/24:
    - send the response back to the frontend -- DONE
    - create npm project, and bring out the react! -- Done 

01/20/24:
    - Finish skeleton chat interface

TODO:
- make sure you use npm project next time

### Frontend inspiration
- https://developer.apple.com/documentation/uikit/supporting-dark-mode-in-your-interface
    - background be as dark, white text
    - gpt response is that magenta color


- Make sure to use lifting state up, instead of making one component control another, store the shared state in the nearest common ancestor and pass it down:
    - the state to the component that needs to display it 
    - the setter function to the component that needs to modify it 

Thoughts:
- for the post-chat endpoint, we are going to do a blocking synchronous call. This is because its easier to implement, and gemini isn't that slow. I guess we can always change this in the future. What that means is that we'll make gemini request, and then return the whole thing with payload, so we'll need the more complicated java class


<cot>
- so the error right now is ObjectMapper, cannot write value as a string, because it's unable to transcribe the object snetchat