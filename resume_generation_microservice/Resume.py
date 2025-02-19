import json
from weasyprint import HTML
import utils
import lxml.etree as ET
from lxml.cssselect import CSSSelector
import lxml.html as html
import datetime
import time 

class Resume:
    def __init__(self, resume_dict):
        ## the starter html 
        html_str = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {{ font-family: "{}", serif; }}
                </style>
            </head>
            <body>
            </body>
            </html>
        """.format(resume_dict["font-name"])
        self.html = ET.fromstring(html_str, parser=ET.HTMLParser())
        self.resume_dict = resume_dict

    def pretty_print(self, element, **kwargs):
        xml = ET.tostring(element, pretty_print=True, **kwargs)
        print(xml.decode(), end='')
        
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
            element = html.fragment_fromstring("\n" + element)
            if append:
                queried_element[0].append(element)
            else:
                queried_element[0].insert(0, element)
            return True
        return False

    def insert_font(self):
        with open(utils.get_relative_path('font_to_link_mapping.json')) as f:
            fonts_mapping = json.load(f)
            # change the body element
            if self.resume_dict['font-name'] in fonts_mapping:
                self.insert_element_query(fonts_mapping[self.resume_dict['font-name']], 'head')

    def generate_pdf_html(self):
        '''
        heavy lifter function to turn json_resume to equivalent html 
        '''
        self.insert_font()
        # for each print through
        for k, v in self.resume_dict.items():
            if isinstance(v, dict) and 'text' in v:
                text = v['text']
                if text:
                    html_element = f"<span>{text}</span>"
                    self.insert_element_query(html_element, 'body')
        #self.pretty_print(self.html)

    def to_pdf(self):
        ''''
        generate_pdf: takes a JSON object, defining a resume, and creates a pdf of that version. Not responsible for validating the resume schema 
        Params:
            json_resume: the resume, as dict 
        Returns:
            (str) path to the generated resume 
        '''
        # at this point, we have the data. What's the key? Create a blank canvas with weasy print
        self.generate_pdf_html()
        now = datetime.datetime.now()
        timestamp = now.strftime('%Y%m%d%H%M%S')
        start = time.time()
        HTML(string=ET.tostring(self.html)).write_pdf(f'../output_resumes/resume{timestamp}.pdf')
        print(time.time() - start)

def main():
    ## just a test function for development purposes 
    # sample path
    resume_template_path = '../resume_templates/simple_1_filled.json'
    with open(resume_template_path, "r") as f:
        resume_dict = json.load(f)
    # create a ResumeGenerator object, where we are going to pass in the json data to generate pdf
    resume = Resume(resume_dict)
    resume.to_pdf()
        
if __name__ == "__main__":
    main()