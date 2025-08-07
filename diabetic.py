from flask import Flask, request, jsonify
import numpy as np
import tensorflow as tf
from tensorflow.keras.preprocessing import image
import os

app = Flask(__name__)

model = tf.keras.models.load_model('diabetic.h5')

@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'error': 'No file provided'}), 400

    file = request.files['file']
    
    
    temp_file_path = 'temp_image.jpg'  
    file.save(temp_file_path)

    
    img = image.load_img(temp_file_path, target_size=(150, 150))  
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0) / 255.0 

    prediction = model.predict(img_array)
    result = 'No Diabetic' if prediction[0] > 0.5 else 'Diabetic'

    os.remove(temp_file_path)

    return jsonify({'prediction': result})

if __name__ == '__main__':
    app.run(port=5003, debug=True) 