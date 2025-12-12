from flask import Flask, request, jsonify
import numpy as np
from PIL import Image
import io
from keras.models import load_model

model = load_model("model.h5")

_, H, W, C = model.input_shape
IMG_SIZE = (W, H)

app = Flask(__name__)

@app.route("/predict", methods=["POST"])
def predict():
    if "file" not in request.files:
        return jsonify({"error": "No file part"}), 200

    f = request.files["file"]
    if f.filename == "":
        return jsonify({"error": "No file selected"}), 200

    try:
        img = Image.open(io.BytesIO(f.read())).convert("L")

        # Resize and normalize
        img = img.resize(IMG_SIZE)
        arr = np.asarray(img, dtype="float32") / 255.0 
        arr = np.expand_dims(arr, axis=-1)             

        if C == 3:
            arr = np.repeat(arr, 3, axis=-1) 

        arr = np.expand_dims(arr, 0) 

        score = model.predict(arr, verbose=0)[0][0]
        label = "Pneumonia" if score > 0.6 else "Normal"
        confidence = round(float(score if label == "Pneumonia" else 1 - score) * 100, 2)

        return jsonify({
            "prediction": label,
            "confidence": confidence,
            "raw_score": float(score)
        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 200

if __name__ == "__main__":
    app.run(debug=True, port=5000)
