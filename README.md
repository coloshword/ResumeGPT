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


Working on:
    - now let's try to generate the first resume. First I need to define a resume template, probably with associated image. By define resume template, i mean write down the template as is the required template. After that, we will try to "simulate" the pdf creation workflow, by filling in the the resume part by hand. After that is done, then I will write the resume template generation code 
  
  - so 2 separate components
    <Filled out template --> Resume pdf generation >
    <Empty resume template --> LLM and user prompting -> filled out resume template 

    Yes, exactly! You’ve captured the essence of the hybrid approach perfectly. Let’s break down the workflow step by step to ensure clarity:

Step-by-Step Workflow
Dynamic Section Identification:

The LLM analyzes the conversation to determine which sections are relevant (e.g., "Education", "Work Experience", "Volunteer Work", "Projects").

Example:

User says: "I have a master’s degree in Computer Science and interned at Google."

LLM infers sections: ["Education", "Work Experience"].

Generate Section-Specific JSON Snippets:

For each inferred section, ask the LLM to generate structured JSON snippets based on the conversation.

Example prompt:

Copy
Generate the "Education" section in JSON format based on this input:  
"I have a master’s degree in Computer Science from Stanford University (2020-2022)."  
Use this template:  
{
  "title": "Education",
  "items": [
    {
      "degree": "",
      "institution": "",
      "duration": "",
      "details": []
    }
  ]
}
Validate Each Snippet:

Use a JSON Schema to validate the structure of each section.

Example schema for the "Education" section:

json
Copy
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "title": { "type": "string" },
    "items": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "degree": { "type": "string" },
          "institution": { "type": "string" },
          "duration": { "type": "string" },
          "details": { "type": "array", "items": { "type": "string" } }
        },
        "required": ["degree", "institution"]
      }
    }
  },
  "required": ["title", "items"]
}
Merge Snippets into the Resume:

Programmatically combine validated sections into the final JSON resume.

Example merged resume:

json
Copy
{
  "personal_info": { "name": "John Doe", "email": "john@example.com" },
  "sections": [
    {
      "title": "Education",
      "items": [
        {
          "degree": "Master’s in Computer Science",
          "institution": "Stanford University",
          "duration": "2020 - 2022",
          "details": ["GPA: 3.9"]
        }
      ]
    },
    {
      "title": "Work Experience",
      "items": [
        {
          "job_title": "Software Engineering Intern",
          "company": "Google",
          "duration": "Summer 2021",
          "details": ["Developed backend APIs for Google Cloud"]
        }
      ]
    }
  ]
}
Why This Works
Flexibility: The LLM can invent new sections (e.g., "Volunteer Work", "Awards") if the user’s input warrants it.

Validation: Ensure data integrity by validating each section against a schema.

Efficiency: Generate sections incrementally as the conversation progresses.

Example Implementation
1. Prompt Engineering
Design prompts to guide the LLM to generate valid JSON. For example:

Copy
You are a resume-building assistant. Generate the "{{SECTION_NAME}}" section in JSON format using the following template.  
User Input: "{{USER_INPUT}}"  

Template:  
{
  "title": "{{SECTION_NAME}}",
  "items": [
    {
      "degree": "",      // Only for "Education"
      "job_title": "",   // Only for "Work Experience"
      "company": "",     // Only for "Work Experience"
      "duration": "",
      "details": []
    }
  ]
}
2. Validation (Python Example)
Use the jsonschema library to validate sections:

python
Copy
from jsonschema import validate

education_schema = {
  "type": "object",
  "properties": {
    "title": { "type": "string" },
    "items": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "degree": { "type": "string" },
          "institution": { "type": "string" },
          "duration": { "type": "string" }
        },
        "required": ["degree", "institution"]
      }
    }
  }
}

# Assume `llm_output` is the JSON snippet from the LLM
try:
    validate(instance=llm_output, schema=education_schema)
    print("Validation passed!")
except Exception as e:
    print(f"Validation failed: {e}")
3. Handling Edge Cases
If the LLM returns invalid JSON, use a fallback mechanism (e.g., ask the user to rephrase or manually correct the data).

For ambiguous sections (e.g., "Leadership" vs. "Management Experience"), let the LLM suggest a section title and allow the user to edit it.

Tools to Simplify This
Python: Use jsonschema for validation and PyYAML/json for parsing.

TypeScript: Use zod or ajv for schema validation.

LLM Function Calling:

OpenAI’s API supports structured outputs (e.g., response_format={ "type": "json_object" }).

Anthropic’s Claude also allows JSON output formatting.

Final Thoughts
Your approach is spot-on! By dynamically generating and validating sections, you’ll create a resume builder that:

Adapts to the user’s unique background.

Ensures data consistency.

Minimizes manual effort.

