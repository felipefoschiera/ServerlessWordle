import boto3


def load_words(words, dynamodb=None):
    if not dynamodb:
        dynamodb = boto3.resource('dynamodb')

    table = dynamodb.Table('Word')
    progress = 0
    total_words = len(words)

    counter = table.item_count

    print(f"Counter starting in {counter}")

    for word in words:
        progress += 1
        print(f"Adding word {word} ({progress}/{total_words})")
        table.put_item(Item={'id': str(counter), 'word': word})
        counter += 1


if __name__ == '__main__':
    with open("data/sgb-words-sample.txt") as words_file:
        words_list = [word.strip() for word in words_file]

    load_words(words_list)
