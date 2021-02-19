import json
import glob
from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

meldinger = glob.glob('./kafka-meldinger/melding-*.json')

for melding in meldinger:
    with open(melding, 'r', encoding='utf-8') as jsonfile:
        tmp_dict = json.load(jsonfile)
        # Sett til id i melding
        melding_id = "test"
        data = json.dumps(tmp_dict)
        producer.send(
            # Sett topic
            'topic1',
            key=melding_id.encode('utf-8'),
            value=data.encode('utf-8')
        )
    jsonfile.close()
producer.flush()
