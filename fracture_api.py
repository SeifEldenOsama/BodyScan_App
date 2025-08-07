# -*- coding: utf-8 -*-
from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np
from PIL import Image
import io

# Load the trained model
model = load_model('fracture.h5')

# Expected image input size
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
        # Load and preprocess the image
        img = Image.open(file.stream).convert('RGB')
        img = img.resize(IMG_SIZE)
        img_array = image.img_to_array(img) / 255.0  # Keep this if model was trained with normalization
        img_array = np.expand_dims(img_array, axis=0)

        # Predict
        prediction = model.predict(img_array)[0][0]  # Single scalar output (sigmoid)

        # Fix: Interpret prediction correctly based on training
        if prediction > 0.5:
            result = "Not Fractured"
            confidence = prediction
        else:
            result = "Fractured"
            confidence = 1 - prediction  # Closer to 1 means more confident it's fractured

        return jsonify({
            'prediction': result,
            'confidence': float(round(confidence, 4))
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5002, debug=True)
