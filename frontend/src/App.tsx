import { FormEvent, useState } from 'react'
const ORDER_API = import.meta.env.VITE_ORDER_API_URL ?? 'http://localhost:8082/api/v1/orders'
export default function App() {
  const [status, setStatus] = useState('Ready to create an order event.')
  function submit(event: FormEvent<HTMLFormElement>) { event.preventDefault(); setStatus('Authentication will be added through the API gateway before live requests are enabled.') }
  return <main><section className="hero"><p className="eyebrow">EVENT-DRIVEN OPERATIONS</p><h1>Commerce Platform</h1><p>React operations console for the Spring Boot and Kafka microservices backend.</p></section><section className="grid"><article><span>01</span><h2>Orders</h2><p>Order Service · port 8082</p></article><article><span>02</span><h2>Inventory</h2><p>Kafka consumer · port 8083</p></article><article><span>03</span><h2>Security</h2><p>OAuth2 / JWT scopes</p></article></section><form onSubmit={submit}><h2>Create test order</h2><label>Product ID<input required placeholder="UUID" /></label><label>Quantity<input required type="number" min="1" defaultValue="1" /></label><button type="submit">Queue order</button><output>{status}</output></form><footer>Configured Order API: {ORDER_API}</footer></main>
}
