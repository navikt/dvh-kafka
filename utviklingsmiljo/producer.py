import json
import glob
from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

sykemeldinger = glob.glob('./sykemeldinger/melding-*.json')

for melding in sykemeldinger:
    with open(melding, 'r', encoding='utf-8') as jsonfile:
        tmp_dict = json.load(jsonfile)
        sykm_id = tmp_dict['sykmelding']['id']
        data = json.dumps(tmp_dict)
        producer.send(
            'privat-syfo-sm2013-automatiskBehandling',
            key=sykm_id.encode('utf-8'),
            value=data.encode('utf-8')
        )
    jsonfile.close()
producer.flush()
