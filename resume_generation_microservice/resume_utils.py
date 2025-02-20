from lxml import etree
from xml.dom.minidom import parseString


def pretty_print_html(element):
    """
    Pretty prints an lxml ElementTree object with proper indentation.
    
    :param element: lxml ElementTree object
    """
    raw_xml = etree.tostring(element, encoding="unicode")  
    parsed_xml = parseString(raw_xml)
    print(parsed_xml.toprettyxml(indent="    "))  