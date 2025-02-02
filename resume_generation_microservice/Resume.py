import json
from weasyprint import HTML
import utils
import lxml.etree as ET
from lxml.cssselect import CSSSelector
import lxml.html as html
import datetime
import time 

### ResumeGenerator: generates a resume from json object
class Resume:
    def __init__(self):
        ## the starter html 
        html_str = '''
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    @font-face {
                        font-family: 'EB Garamond';
                        src: url('fonts/EBGaramond-VariableFont_wght.ttf') format('truetype');
                    }
                    body { font-family: "EB Garamond", serif; }
                    h1 { color: red; }
                </style>
            </head>
            <body>
                <h1>Hello, PDF!</h1>
            </body>
            </html>
        '''
        self.html = ET.fromstring(html_str, parser=ET.HTMLParser())

    def pretty_print(self, element, **kwargs):
        xml = ET.tostring(element, pretty_print=True, **kwargs)
        print(xml.decode(), end='')

    def insert_font_style(self):
        pass
        # based on the font-style, insert the right type of font to it
        
    def insert_element_query(self, element, query, append=True):
        '''
        inserts a new element, as a string, as a child of the first element that matches the query. 
            Params:
                html
                element: string; the element as a string 
                query: string; the query as a string (equivalent to query selector in js land)
            Returns:
                success? --> True is successful, False if not 
        '''
        # first start by finding the element based on query
        queried_element = CSSSelector(query)(self.html)
        if len(queried_element) > 0:
            element = html.fragment_fromstring(element)
            if append:
                queried_element[0].append(element)
            else:
                queried_element[0].insert(0, element)
            return True
        return False


    def generate_pdf_html(self, json_resume):
        '''
        heavy lifter function to turn json_resume to equivalent html 
        ''' 
        with open(utils.get_relative_path('font_to_link_mapping.json')) as f:
            fonts_mapping = json.load(f)
        if json_resume['font-name'] in fonts_mapping:
            #self.insert_element_query(fonts_mapping[json_resume['font-name']], 'head')
            self.pretty_print(self.html)

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
        start = time.time()
        HTML(string=ET.tostring(self.html)).write_pdf(f'resume{timestamp}.pdf')
        print(time.time() - start)
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