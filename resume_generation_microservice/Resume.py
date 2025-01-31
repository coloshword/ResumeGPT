import json
from weasyprint import HTML
import utils 
from bs4 import BeautifulSoup
import datetime

### ResumeGenerator: generates a resume from json object
class Resume:
    def __init__(self):
        ## the starter html 
        html_str = '''
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: sans-serif; }
                    h1 { color: red; }
                </style>
            </head>
            <body>
                <h1>Hello, PDF!</h1>
            </body>
            </html>
        '''
        self.html = BeautifulSoup(html_str, 'html.parser')
    
    def insert_element_query(self, element, query):
        '''
        inserts a new element, as a string, as a child of the first element that matches the query. 
            Params:
                html
                element: string; the element as a string 
                query: string; the query as a string (equivalent to query selector in js land)
        '''
        element = self.html.select(query).append(BeautifulSoup(element, 'html.parser'))
        print(str(element))

    def generate_pdf_html(self, json_resume):
        '''
        heavy lifter function to turn json_resume to equivalent html 
        ''' 
        with open(utils.get_relative_path('font_to_link_mapping.json')) as f:
            fonts_mapping = json.load(f)
        if json_resume['font-name'] in fonts_mapping:
            print(fonts_mapping[json_resume['font-name']])
            self.insert_element_query(fonts_mapping[json_resume['font-name']], 'head')

    def to_pdf(self, json_resume):
        ''''
        generate_pdf: takes a JSON object, defining a resume, and creates a pdf of that version. Not responsible for validating the resume schema 
        Params:
            json_resume: the resume, as dict 
        Returns:
            (str) path to the generated resume 
        '''
        # at this point, we have the data. What's the key? Create a blank canvas with weasy print
        self.generate_pdf_html(json_resume)
        now = datetime.datetime.now()
        timestamp = now.strftime('%Y%m%d%H%M%S')
        HTML(string=str(self.html)).write_pdf(f'resume{timestamp}.pdf')
        # so it looks like the key function is write_pdf, and we can write it however we like using html. Let's just try to see this in action right now 

def main():
    ## just a test function for development purposes 
    # sample path
    resume_template_path = '../resume_templates/simple_1_filled.json'
    with open(resume_template_path, "r") as f:
        data = json.load(f)

    # create a ResumeGenerator object, where we are going to pass in the json data to generate pdf
    resume = Resume()
    resume.to_pdf(data)
        
if __name__ == "__main__":
    main()