from flask import Flask, request, jsonify
from flask_cors import CORS
import random

app = Flask(__name__)
CORS(app)  # Allow cross-origin requests

# Simple AI response function
def generate_ai_response(user_input):
    responses = [
        "I'm here to listen.",
        "That sounds interesting!",
        "Tell me more!",
        "I understand how you feel."
        "I'm glad you're talking to me."
    ]
    return random.choice(responses)

# API endpoint to handle chat requests
@app.route('/chat', methods=['POST'])
def chat():
    data = request.json  # Get the JSON data sent from the Android app
    user_message = data.get('message', '')

    if user_message:
        ai_response = generate_ai_response(user_message)
        return jsonify({"response": ai_response}), 200
    else:
        return jsonify({"error": "Message is empty!"}), 400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
