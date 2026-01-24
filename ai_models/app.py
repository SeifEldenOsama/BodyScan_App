import os
import io
import numpy as np
from flask import Flask, request, jsonify
from flask_cors import CORS
from PIL import Image
from keras.models import load_model

app = Flask(__name__)
CORS(app)

# Configuration
MODELS_CONFIG = {
    "chest": {"path": "model_chest.h5", "label_map": {0: "Normal", 1: "Pneumonia"}, "threshold": 0.6},
    "brain": {"path": "model_brain.h5", "label_map": {0: "No Tumor", 1: "Tumor"}, "threshold": 0.5},
    "diabetic": {"path": "model_diabetic.h5", "label_map": {0: "No DR", 1: "DR"}, "threshold": 0.5},
    "fracture": {"path": "model_fracture.h5", "label_map": {0: "No Fracture", 1: "Fracture"}, "threshold": 0.5}
}

# Global dictionary to store loaded models
loaded_models = {}

def get_model(model_key):
    if model_key not in loaded_models:
        model_path = MODELS_CONFIG[model_key]["path"]
        if os.path.exists(model_path):
            loaded_models[model_key] = load_model(model_path)
        else:
            return None
    return loaded_models[model_key]

def preprocess_image(image_bytes, target_size, channels):
    img = Image.open(io.BytesIO(image_bytes)).convert("L" if channels == 1 else "RGB")
    img = img.resize(target_size)
    arr = np.asarray(img, dtype="float32") / 255.0
    if channels == 1:
        arr = np.expand_dims(arr, axis=-1)
    arr = np.expand_dims(arr, 0)
    return arr

@app.route("/predict/<model_type>", methods=["POST"])
def predict(model_type):
    if model_type not in MODELS_CONFIG:
        return jsonify({"error": f"Invalid model type: {model_type}"}), 400

    if "file" not in request.files:
        return jsonify({"error": "No file part"}), 400

    f = request.files["file"]
    if f.filename == "":
        return jsonify({"error": "No file selected"}), 400

    try:
        model = get_model(model_type)
        if model is None:
            return jsonify({"error": f"Model file for {model_type} not found"}), 500

        _, H, W, C = model.input_shape
        img_bytes = f.read()
        processed_img = preprocess_image(img_bytes, (W, H), C)

        prediction = model.predict(processed_img, verbose=0)[0][0]
        
        config = MODELS_CONFIG[model_type]
        label = config["label_map"][1] if prediction > config["threshold"] else config["label_map"][0]
        confidence = float(prediction if label == config["label_map"][1] else 1 - prediction)

        return jsonify({
            "model": model_type,
            "prediction": label,
            "confidence": round(confidence * 100, 2),
            "raw_score": float(prediction)
        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/health", methods=["GET"])
def health():
    return jsonify({"status": "healthy", "models_available": list(MODELS_CONFIG.keys())}), 200

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5000))
    app.run(host="0.0.0.0", port=port, debug=True)
