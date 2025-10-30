CREATE TABLE IF NOT EXISTS entries (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID,
  name TEXT NOT NULL,
  barcode TEXT,
  qty INTEGER DEFAULT 0,
  expires DATE,
  created_at TIMESTAMPTZ DEFAULT now()
);
