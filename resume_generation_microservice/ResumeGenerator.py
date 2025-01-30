import json
from weasyprint import HTML
### ResumeGenerator: generates a resume from json object
class ResumeGenerator:
    def __init__(self):
        pass

    def generate_pdf(json_resume):
        ''''
        generate_pdf: takes a JSON object, defining a resume, and creates a pdf of that version. Not responsible for validating the resume schema 
        Params:
            json_resume: the resume, as dict 
        Returns:
            (str) path to the generated resume 
        '''
        # at this point, we have the data. What's the key? Create a blank canvas with weasy print
        # so it looks like the key function is write_pdf, and we can write it however we like using html. Let's just try to see this in action right now 


def main():
    ## just a test function for development purposes 
    # sample path
    resume_template_path = '../resume_templates/simple_1_filled.json'
    with open(resume_template_path, "r") as f:
        data = json.load(f)

    # create a ResumeGenerator object, where we are going to pass in the json data to generate pdf
    generator = ResumeGenerator()
    generator.generate_pdf(data)
        

if __name__ == "__main__":
    main()