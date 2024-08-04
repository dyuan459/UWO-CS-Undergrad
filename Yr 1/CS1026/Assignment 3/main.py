"""
Danry Yuan
251368314
yyuan459
11/18/2023

Takes input about the file user wants to use, uses function to calculate average sentiment of those
files and output to a specified file.
"""
# import the sentiment_analysis module
from sentiment_analysis import *

def main():
    '''
    Main function
    :return: nothing, only outputs and writes to file
    '''
    keyword_file = input("Input keyword filename (.tsv file): ")
    if not keyword_file.endswith(".tsv"):
        raise Exception("Exception: Must have tsv file extension!")
    tweet_file = input("Input tweet filename (.csv file): ")
    if not tweet_file.endswith(".csv"):
        raise Exception("Exception: Must have csv file extension!")
    output_file = input("Input filename to output report in (.txt file): ")
    if not output_file.endswith(".txt"):
        raise Exception("Exception: Must have txt file extension!")
    key_dict = read_keywords(keyword_file)
    tweet_list = read_tweets(tweet_file)
    report = make_report(tweet_list, key_dict)
    write_report(report, output_file)

main()