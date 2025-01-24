const amqp = require('amqplib');  // Import amqplib for RabbitMQ
const QUEUE_NAME = 'device_measurements';  // Define the queue name

// Function to connect to RabbitMQ and consume messages from the queue
async function connectToQueue() {
    try {
        // Connect to RabbitMQ server
        const connection = await amqp.connect('amqp://localhost');
        const channel = await connection.createChannel();

        // Assert that the queue exists
        await channel.assertQueue(QUEUE_NAME, {
            durable: true,  // Ensures the queue survives server restarts
        });

        // Consume messages from the queue
        console.log(`Waiting for messages in ${QUEUE_NAME}...`);
        channel.consume(QUEUE_NAME, (msg) => {
            if (msg !== null) {
                const message = JSON.parse(msg.content.toString());
                console.log('Received message:', message);

                // Check if energy consumption exceeds a limit
                if (message.energy > 100) {
                    console.log('Energy consumption exceeds the limit!');
                    // You can push notifications or take action here
                }

                // Acknowledge the message as processed
                channel.ack(msg);
            }
        });
    } catch (error) {
        console.error('Error connecting to the queue:', error);
    }
}

// Start the consumer
connectToQueue();
