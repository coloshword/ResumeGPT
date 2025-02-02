### ResumeGPT 
- Create a resume, simply by talking to an LLM
- LLM asks a seires of questions until enough information is gathereed to create a resume

### Stack
React Frontend (User Interaction)
  │
  POST /api/generate-resume
  ▼
Java Spring Backend (Validates/Processes Data, Calls LLM)
  │
  POST http://python-pdf-service/generate-pdf (Internal Call)
  ▼
Python PDF Service (Generates PDF)
  │
  Returns PDF bytes to Java
  ▼
Java Sends PDF as Response to Frontend


### Implementation milestones 
- 01/14: First spring Get and Post request 

- 01/16: First API call to Gemini Flash

- 01/18: API call returns back to the frontend 

-01/21:
    Client sends post-chat request to frontend, and adds it to the frontend

- 01/23:
    - refactored endpoints with more sophisticated payload 

01/30:
    - started skeleton of python microservice for resume generation

### TODO:
- make LLM backend "have context"


### Frontend inspiration
- https://developer.apple.com/documentation/uikit/supporting-dark-mode-in-your-interface
    - background be as dark, white text
    - gpt response is that magenta color


- Make sure to use lifting state up, instead of making one component control another, store the shared state in the nearest common ancestor and pass it down:
    - the state to the component that needs to display it 
    - the setter function to the component that needs to modify it 



time:
5.2886 seconds 

