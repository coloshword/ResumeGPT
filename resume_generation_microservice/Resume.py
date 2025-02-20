import json
from weasyprint import HTML
import utils
from lxml.cssselect import CSSSelector
import lxml.html as html
import datetime
import lxml.etree as ET
import time
import resume_utils

class Resume:
    def __init__(self, resume_dict):
        ## the starter html 
        html_str = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {{ font-family: "{}", serif; }}
                    .resume {{
                        width: 100%;
                        height: 100%;
                        display: flex;
                        flex-direction: column;
                    }}
                </style>
            </head>
            <body>
                <div class="resume">
                </div>
            </body>
            </html>
        """.format(resume_dict["font-name"])
        self.html = ET.fromstring(html_str, parser=ET.HTMLParser())
        self.resume_dict = resume_dict
        
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

    def create_text_element(self, text_element_json):
        '''
        create_text_element: creates, and styles a text element. A text element is a json object with the value "text" in it, with associated fields 
        Params:
            text_element_json: the text element as json
        Returns:
            (str) the string representing the HTML element that is the "text node"
        '''
        print(text_element_json)
        # text is the key value here, so why not start with that, we just input the text as value 
        # the next key is stylings, the next is there are styles, and font-size. 
        # I can assume that these elements will max out at this, or just a few more, because this is a resume generator, we don't need to make it very flexible
        # well we can generate the inline styles sooner this way! 
        styles = []
        if text_element_json["font-size"]:
            styles.append(f"font-size:{text_element_json["font-size"]}px")
        if text_element_json["font-weight"]:
            styles.append(f"font-weight:{text_element_json["font-weight"]}") 
        if text_element_json["text"]:
            # it can't just be an empty value 
            # style string 
            style_string = ";".join(styles)
            return f"<span style=\"{style_string}\">{text_element_json["text"]}</span>"

    def generate_pdf_html(self):
        '''
        generate_pdf_html: creates and generates the pdf html
        Params:
        Returns:
            (None), but modifies the internal html value to reflect the generated html
        '''
        self.insert_font()
        # for each print through
        for k, v in self.resume_dict.items():
            if isinstance(v, dict) and 'text' in v:
                # this is a text element, so handle_text generation
                self.insert_element_query(self.create_text_element(v), '.resume')
                
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
        resume_utils.pretty_print_html(self.html)
        HTML(string=ET.tostring(self.html)).write_pdf(f'../output_resumes/resume{timestamp}.pdf')
        print(time.time() - start)

def main():
    ## just a test function for development purposes 
    # sample path
    resume_template_path = '../resume_templates/text_only.json'
    with open(resume_template_path, "r") as f:
        resume_dict = json.load(f)
    # create a ResumeGenerator object, where we are going to pass in the json data to generate pdf
    resume = Resume(resume_dict)
    resume.to_pdf()
        
if __name__ == "__main__":
    main()