"""
Danry Yuan
251368314
yyuan459
11/18/2023

Functions to analyze user specified files in main.py
"""
def read_csv(csv_file_name):
    '''
    read csv file
    :param csv_file_name: name of the file to be read
    :return: list of text split by comma with \n removed
    '''
    text_list = []
    try:
        csv = open(csv_file_name, "r")
        for n in csv:
            # get rid of \n just in case and split into list by commas since comma seperated
            n = n.replace("\n", "")
            text_list.append(n.split(","))
        csv.close()
        return text_list
    except:
        print(f"Could not open file {csv_file_name}")
        return []

def read_tsv(tsv_file_name):
    '''
    Read tsv files
    :param tsv_file_name: name of file to be read
    :return: list of text split by \t
    '''
    text_list = {}
    tsv = open(tsv_file_name, "r")
    for n in tsv:
        n = n.replace("\n", "")
        temp = n.split("\t")
        text_list.update({temp[0]:temp[1]})
    tsv.close()
    return text_list

def read_keywords(keywords_file_name):
    '''
    Read the keys file and create a dictionary from there
    :return: dictionary of keys and values
    '''
    keys_dict = {}
    keys = ""
    try:
        keys = read_tsv(keywords_file_name)
    except IOError:
        print(f"Could not open file {keywords_file_name}!")
    for n in keys:
        keys_dict.update({n:int(keys[n])})
    return keys_dict

def read_tweets(tweets_file_name):
    '''
    read the tweet csv file
    :param tweets_file_name: name of the file to be read
    :return: list of dictionaries with appropriate key-value pairs
    '''
    tweet_dict = {}
    tweet_list = []
    tweets = read_csv(tweets_file_name)
    for n in tweets:
        # try to make latitude and longitude floats but if value error is raised, meaning it has
        # letters meaning it can only be NULL just carry on the loop.
        try:
            n[9] = float(n[9])
        except ValueError:
            pass
        try:
            n[10] = float(n[10])
        except ValueError:
            pass
        tweet_dict.update({"date":n[0], "text":clean_tweet_text(n[1]), "user":n[2],
                           "retweet":int(n[3]), "favorite":int(n[4]), "lang":n[5],
                           "country":n[6], "state":n[7], "City":n[8], "lat":n[9],
                           "lon":n[10]})
        tweet_list.append(tweet_dict.copy())
    return tweet_list



def clean_tweet_text(str):
    '''
    Clean up strings
    :param str: input string
    :return: cleaned string
    '''
    # every special character I can think of and find
    special_characters = ".,\\\'\"[@_!#$%^&*()<>?/|~:]0123456789{}+=-"
    str = str.lower()
    # loop through special characters and remove such characters from input str
    for n in special_characters:
        str = str.replace(n, "")
    return str

def calc_sentiment(str, keys):
    '''
    calculate sentiment of a string by dictionary of keywords
    :param str: string to be calculated
    :param keys: dictionary of keywords
    :return: average sentiment score of tweet
    '''
    score = 0
    word_list = clean_tweet_text(str)
    word_list = word_list.split(" ")
    for n in word_list:
        # try to call a value with the key of a word in word list and if that does not exist go to
        # next iteration
        try:
            score += keys[n]
        except KeyError:
            continue
    return int(round(score))

def classify(score):
    '''
    classify positive, negative, neutral by score
    :param score: integer to be classified
    :return: string of classification
    '''
    if score > 0:
        return "positive"
    elif score < 0:
        return "negative"
    else:
        return "neutral"

def make_report(tweet_list, keyword_dict):
    '''
    make dictionary for a report with appropriate keys and values
    :param tweet_list: list of dictionaries of tweet info
    :param keyword_dict: dictionary of keywords
    :return: dictionary for a report
    '''
    avg_sentiment = 0.0
    num_favorite = 0
    num_negative = 0
    num_neutral = 0
    num_positive = 0
    num_retweet = 0
    num_tweets = len(tweet_list)
    country_dict = {}
    count_dict = {}
    # loop through tweet list
    for n in tweet_list:
        # keep track of sentiment score, country and classification of the tweet this iteration which
        # will be replaced in the next
        temp_int = calc_sentiment(n["text"], keyword_dict)
        temp_str = classify(temp_int)
        country = n["country"]
        if temp_str == "negative":
            num_negative += 1
        elif temp_str == "neutral":
            num_neutral += 1
        else:
            num_positive += 1
        # keep track of total sentiment to be averaged out in the end
        avg_sentiment += temp_int
        try:
            country_dict[country] += temp_int # try to add to the sentiment score of this
            # iteration's country with this iteration's sentiment score
            count_dict[country] += 1 # try to increase the count of this iteration's country
        except KeyError:
            country_dict.update({country:temp_int}) # if this iteration's country has not been
            # recorded yet update country dictionary
            count_dict.update({country:1}) # same deal but with this country's count
    for c in country_dict:
        country_dict[c] /= count_dict[c] # loop through country dictionary and average their values
        # by total count
    top_five = top_five_sort(country_dict)
    for f in tweet_list:
        if f["favorite"] > 0:
            num_favorite += 1
        if f["retweet"] > 0:
            num_retweet += 1
    avg_sentiment = avg_sentiment/len(tweet_list)
    report_dict = {"avg_favorite":average_sentiment(tweet_list, "favorite", keyword_dict),
                   "avg_retweet":average_sentiment(tweet_list, "retweet", keyword_dict),
                   "avg_sentiment":round(avg_sentiment, 2), "num_favorite":num_favorite,
                   "num_negative":num_negative, "num_neutral":num_neutral, "num_positive":num_positive,
                   "num_retweet":num_retweet, "num_tweets":num_tweets, "top_five":top_five}
    return report_dict

def top_five_sort(country_dict):
    '''
    sort dictionary of countries with average sentiment values from most to least
    :param country_dict: dictionary of countries and values of their average sentiment
    :return: ranked, sorted and cleaned string
    '''
    top_five = ""
    # loop as many times as the country dictionary's size
    for x in range(len(country_dict)):
        compare_int = -1000000 # initial comparison int; all numbers coming in are averaged and tweets
        # have a character limit so realistically this will be lower than any country's sentiment score
        compare_str = "" # initialize compare string
        for c in country_dict:
            # if this country's value is greater than the comparison int and the country isn't null
            # then replace comparison int with this country's score
            if country_dict[c] > compare_int and c != "NULL":
                compare_int = float(country_dict[c])
                compare_str = str(c)
        # if this is not the sixth loop+, the compare string is not null nor blank add this
        # iteration's compare string which is the country with the highest score to the top five
        # string then delete that country from the dictionary
        if (x <= 4) and (compare_str != "NULL") and (compare_str != ""):
            top_five += f"{compare_str}, "
            del country_dict[compare_str]
        # don't loop more than 5 times
        elif x == 5:
            break
    # at the end strip comma and space on the right and return a string of top five countries
    return top_five.rstrip(", ")

def write_report(report, output_file):
    '''
    write report to a txt file
    :param report: dictionary of required statistics
    :param output_file: file to be written to
    :return: nothing but a print and edits a file
    '''
    try:
        output = open(output_file, "w")
        output.write(f"""Average sentiment of all tweets: {report["avg_sentiment"]} 
Total number of tweets: {report["num_tweets"]}
Number of positive tweets: {report["num_positive"]} 
Number of negative tweets: {report["num_negative"]} 
Number of neutral tweets: {report["num_neutral"]} 
Number of favorited tweets: {report["num_favorite"]} 
Average sentiment of favorited tweets: {report["avg_favorite"]} 
Number of retweeted tweets: {report["num_retweet"]} 
Average sentiment of retweeted tweets: {report["avg_retweet"]} 
Top five countries by average sentiment: {report["top_five"]}""")
        output.close()
        print(f"Wrote report to {output_file}")
    except IOError:
        print(f"Could not open file {output_file}")


def average_sentiment(tweet_list, key, keyword_dict):
    '''
    calculate average sentiment of a specified key with more than 0
    :param tweet_list: list of dictionary of tweets
    :param key: specified key
    :param keyword_dict: dictionary of keywords
    :return: rounded average sentiment
    '''
    average = 0.0
    count = 0
    for n in tweet_list:
        if n[key] > 0:
            average += calc_sentiment(n["text"], keyword_dict)
            count += 1
    if average == 0.0:
        return 0.000
    return round(average/count, 2)