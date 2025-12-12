from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np
from PIL import Image
import io

model = load_model('fracture.h5')

IMG_SIZE = (224, 224)

app = Flask(__name__)

@app.route('/')
def home():
    return "Bone Fracture Detection API (Running on Port 5002)"

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'error': 'No file uploaded'}), 400

    file = request.files['file']

    try:
        img = Image.open(file.stream).convert('RGB')
        img = img.resize(IMG_SIZE)
        img_array = image.img_to_array(img) / 255.0
        img_array = np.expand_dims(img_array, axis=0)

        prediction = model.predict(img_array)[0][0]

        if prediction > 0.5:
            result = "Not Fractured"
            confidence = prediction
        else:
            result = "Fractured"
            confidence = 1 - prediction

        return jsonify({
            'prediction': result,
            'confidence': float(round(confidence, 4))
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5002, debug=True)
